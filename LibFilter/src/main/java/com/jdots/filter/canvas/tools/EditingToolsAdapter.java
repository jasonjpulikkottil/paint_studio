package com.jdots.filter.canvas.tools;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdots.filter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.2
 * @since 5/23/2018
 */
public class EditingToolsAdapter extends RecyclerView.Adapter<EditingToolsAdapter.ViewHolder> {

    private List<ToolModel> mToolList = new ArrayList<>();
    private OnItemSelected mOnItemSelected;

    public EditingToolsAdapter(OnItemSelected onItemSelected) {
        mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModel("Brush", R.drawable.ic_brush, FineToolType.BRUSH));
        mToolList.add(new ToolModel("Text", R.drawable.ic_text, FineToolType.TEXT));
        mToolList.add(new ToolModel("Eraser", R.drawable.ic_eraser, FineToolType.ERASER));
        mToolList.add(new ToolModel("Filter", R.drawable.ic_photo_filter, FineToolType.FILTER));
        mToolList.add(new ToolModel("Emoji", R.drawable.ic_insert_emoticon, FineToolType.EMOJI));
        mToolList.add(new ToolModel("Sticker", R.drawable.ic_sticker, FineToolType.STICKER));
    }
 
    public interface OnItemSelected {
        void onToolSelected(FineToolType fineToolType);
    }

    class ToolModel {
        private String mToolName;
        private int mToolIcon;
        private FineToolType mFineToolType;

        ToolModel(String toolName, int toolIcon, FineToolType fineToolType) {
            mToolName = toolName;
            mToolIcon = toolIcon;
            mFineToolType = fineToolType;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_editing_tools, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolModel item = mToolList.get(position);
        holder.txtTool.setText(item.mToolName);
        holder.imgToolIcon.setImageResource(item.mToolIcon);
    }

    @Override
    public int getItemCount() {
        return mToolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(View itemView) {
            super(itemView);
            imgToolIcon = itemView.findViewById(R.id.imgToolIcon);
            txtTool = itemView.findViewById(R.id.txtTool);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelected.onToolSelected(mToolList.get(getLayoutPosition()).mFineToolType);
                }
            });
        }
    }
}
