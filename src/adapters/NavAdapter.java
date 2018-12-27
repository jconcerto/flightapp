package adapters;

import com.flightapp.androidproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An adapter class which creates a Navigation drawer at the top right of
 * the screen.
 *
 */
public class NavAdapter extends BaseAdapter {
	

	private Context context;
    private String[] drawerTitles;
    int[] images = { R.drawable.ic_action_person, R.drawable.ic_action_search, 
    		R.drawable.ic_action_airplane_mode_off, R.drawable.ic_action_remove };
    
    /**
     * Constructs a new NavAdapter.
     * @param context The context of the activity to display this adapter view.
     */
    public NavAdapter(Context context) {
    	this.context = context;
    	drawerTitles = context.getResources().getStringArray(R.array.nav_client_ops);
    }

	@Override
	public int getCount() {
		return drawerTitles.length;
	}

	@Override
	public Object getItem(int position) {
		return drawerTitles[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.drawer_row, parent, false);
		}
		else {
			row = convertView;
		}
		
		TextView titleText = (TextView) row.findViewById(R.id.row_text);
		ImageView titleImage = (ImageView) row.findViewById(R.id.row_image);
		titleText.setText(drawerTitles[position]);
		titleImage.setImageResource(images[position]);
		return row;
	}
	
}
