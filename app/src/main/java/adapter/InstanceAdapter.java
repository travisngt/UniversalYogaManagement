package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.universalyoga_management.R;
import com.example.universalyoga_management.models.ClassInstance;

import java.util.ArrayList;

public class InstanceAdapter extends ArrayAdapter<ClassInstance> {

    public InstanceAdapter(Context context, ArrayList<ClassInstance> instances) {
        super(context, 0, instances);
    }

    // ViewHolder pattern for improved performance
    static class ViewHolder {
        TextView instanceDate;
        TextView teacher;
        TextView comments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ClassInstance instance = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_class_instance, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.instanceDate = convertView.findViewById(R.id.instanceDate);
            viewHolder.teacher = convertView.findViewById(R.id.teacher);
            viewHolder.comments = convertView.findViewById(R.id.comments);
            convertView.setTag(viewHolder); // Store the holder with the view
        } else {
            viewHolder = (ViewHolder) convertView.getTag(); // Reuse the holder
        }

        // Populate the data into the template view using the data object
        if (instance != null) { // Null check
            viewHolder.instanceDate.setText(instance.getInstanceDate());
            viewHolder.teacher.setText(instance.getTeacher());
            viewHolder.comments.setText(instance.getComments());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
