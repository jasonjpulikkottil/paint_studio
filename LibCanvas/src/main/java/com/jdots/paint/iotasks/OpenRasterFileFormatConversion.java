package com.jdots.paint.iotasks;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.jdots.paint.common.Constants;
import com.jdots.paint.FileIO;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//
//Source:	https://www.openraster.org/baseline/file-layout-spec.html
//
//Layout:
//example.ora  [considered as a folder-like object]
//        ├ mimetype
//        ├ stack.xml
//        ├ data/
//        │  ├ [image data files referenced by stack.xml, typically layer*.png]
//        │  └ [other data files, indexed elsewhere]
//        ├ Thumbnails/
//        │  └ thumbnail.png
//        └ mergedimage.png
public final class OpenRasterFileFormatConversion {
	private static final String TAG = OpenRasterFileFormatConversion.class.getSimpleName();
	private static final int COMPRESS_QUALITY = 100;
	private static final int THUMBNAIL_WIDTH = 256;
	private static final int THUMBNAIL_HEIGHT = 256;

	private OpenRasterFileFormatConversion() {
		throw new AssertionError();
	}

	public static Uri exportToOraFile(List<Bitmap> bitmapList, String fileName, Bitmap bitmapAllLayers, ContentResolver resolver) throws IOException {
		Uri imageUri;
		OutputStream outputStream;
		ContentValues contentValues = new ContentValues();
		float wholeSize = 0;

		String mimeType = "image/openraster";
		byte[] mimeByteArray = mimeType.getBytes();
		byte[] xmlByteArray = getXmlStack(bitmapList);

		for (Bitmap current: bitmapList) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			current.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, bos);
			byte[] byteArray = bos.toByteArray();
			wholeSize += byteArray.length;
		}

		ByteArrayOutputStream bosMerged = new ByteArrayOutputStream();
		bitmapAllLayers.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, bosMerged);
		byte[] bitmapByteArray = bosMerged.toByteArray();

		Bitmap bitmapThumb = Bitmap.createScaledBitmap(bitmapAllLayers, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, false);
		ByteArrayOutputStream bosThumb = new ByteArrayOutputStream();
		bitmapThumb.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, bosThumb);
		byte[] bitmapThumbArray = bosThumb.toByteArray();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

			//applefile has no file ending. which is important for api level 30.
			// we can't save an application file in media directory so we have to save it in downloads.
			contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
			contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
			contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/applefile");

			imageUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
			outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
		} else {
			contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
			contentValues.put(MediaStore.Images.Media.MIME_TYPE, "application/zip");
			contentValues.put(MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MEDIA_TYPE_NONE);

			long date = System.currentTimeMillis();
			contentValues.put(MediaStore.MediaColumns.DATE_MODIFIED, date / 1000);

			wholeSize += xmlByteArray.length;
			wholeSize += mimeByteArray.length;
			wholeSize += bitmapByteArray.length;
			wholeSize += bitmapThumbArray.length;
			contentValues.put(MediaStore.Images.Media.SIZE, wholeSize);
			imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
			outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
		}

		ZipOutputStream streamZip = new ZipOutputStream(outputStream);
		ZipEntry mimetypeEntry = new ZipEntry("mimetype");
		mimetypeEntry.setMethod(ZipEntry.DEFLATED);

		streamZip.putNextEntry(mimetypeEntry);
		streamZip.write(mimeByteArray, 0, mimeByteArray.length);
		streamZip.closeEntry();

		streamZip.putNextEntry(new ZipEntry("stack.xml"));
		streamZip.write(xmlByteArray, 0, Objects.requireNonNull(xmlByteArray).length);
		streamZip.closeEntry();

		int counter = 0;
		for (Bitmap current: bitmapList) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			current.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, bos);
			byte[] byteArray = bos.toByteArray();

			streamZip.putNextEntry(new ZipEntry("data/layer" + counter + ".png"));
			streamZip.write(byteArray, 0, byteArray.length);
			streamZip.closeEntry();
			counter++;
		}

		streamZip.putNextEntry(new ZipEntry("Thumbnails/thumbnail.png"));
		streamZip.write(bitmapThumbArray, 0, bitmapThumbArray.length);
		streamZip.closeEntry();

		streamZip.putNextEntry(new ZipEntry("mergedimage.png"));
		streamZip.write(bitmapByteArray, 0, bitmapByteArray.length);
		streamZip.closeEntry();
		streamZip.close();
		outputStream.close();

		return imageUri;
	}

	private static byte[] getXmlStack(List<Bitmap> bitmapList) {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("image");
			Attr attr1 = doc.createAttribute("version");
			Attr attr2 = doc.createAttribute("w");
			Attr attr3 = doc.createAttribute("h");

			attr1.setValue("0.0.1");
			attr2.setValue(String.valueOf(bitmapList.get(0).getWidth()));
			attr3.setValue(String.valueOf(bitmapList.get(0).getHeight()));

			rootElement.setAttributeNode(attr1);
			rootElement.setAttributeNode(attr2);
			rootElement.setAttributeNode(attr3);
			doc.appendChild(rootElement);

			Element stack = doc.createElement("stack");
			rootElement.appendChild(stack);

			for (int i = bitmapList.size() - 1; i >= 0; i--) {
				Element layer = doc.createElement("layer");

				layer.setAttribute("name", "layer" + i);
				layer.setAttribute("src", "data/layer" + i + ".png");
				stack.appendChild(layer);
			}

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(stream);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			transformer.transform(source, result);

			return stream.toByteArray();
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "Could not create document.");
			return null;
		} catch (TransformerException e) {
			Log.e(TAG, "Could not transform Xml file.");
			return null;
		}
	}

	public static Uri saveOraFileToUri(List<Bitmap> bitmapList, Uri uri, String fileName, Bitmap bitmapAllLayers, ContentResolver resolver) throws IOException {
		String[] projection = {MediaStore.Images.Media._ID};

		Cursor c = resolver.query(uri, projection, null, null, null);
		if (c.moveToFirst()) {
			long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));

			Uri deleteUri;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				deleteUri = ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id);
			} else {
				deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
			}

			resolver.delete(deleteUri, null, null);
		} else {
			throw new AssertionError("No file to delete was found!");
		}
		c.close();

		return exportToOraFile(bitmapList, fileName, bitmapAllLayers, resolver);
	}

	public static List<Bitmap> importOraFile(ContentResolver resolver, Uri uri, int maxWidth, int maxHeight, boolean scaled) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		InputStream inputStream = resolver.openInputStream(uri);
		ZipInputStream zipInput = new ZipInputStream(inputStream);

		options.inMutable = true;
		options.inJustDecodeBounds = false;
		if (scaled) {
			options.inSampleSize = FileIO.calculateSampleSize(options.outWidth, options.outHeight,
					maxWidth, maxHeight);
		}

		ZipEntry current = zipInput.getNextEntry();
		List<Bitmap> bitmapList = new ArrayList<>();
		while (current != null) {
			if (current.getName().matches("data/(.*).png")) {

				Bitmap layerBitmap = FileIO.enableAlpha(BitmapFactory.decodeStream(zipInput, null, options));
				bitmapList.add(layerBitmap);
			}
			current = zipInput.getNextEntry();
		}

		if (bitmapList.isEmpty() || bitmapList.size() > Constants.MAX_LAYERS) {
			throw new IOException("Bitmap list is wrong!");
		}

		return bitmapList;
	}
}
