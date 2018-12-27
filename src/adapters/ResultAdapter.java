package adapters;

import java.util.List;

import bookingApp.Itinerary;
import com.flightapp.androidproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * An adapter class which creates a ListView of Itinerary results 
 * to display for the user.
 *
 */
public class ResultAdapter extends BaseAdapter {
	
	Context context;
	List<Itinerary> itinList;
	
    /**
     * Constructs a new ResultAdapter.
     * @param context The context of the Activity to display this adapter.
     * @param resultlist The list of Itineraries to display.
     */
    public ResultAdapter(Context context, List<Itinerary> resultlist) {
    	this.context = context;
    	itinList = resultlist;
    }
    
	@Override
	public int getCount() {
		return itinList.size();
	}

	@Override
	public Object getItem(int position) {
		return itinList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Create row views of results by setting the TextView for each
		// needed item.
		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.result_row, parent, false);
		}
		else {
			row = convertView;
		}
		
		TextView fromTime = (TextView) row.findViewById(R.id.rr_from_text);
		TextView toTime = (TextView) row.findViewById(R.id.rr_to_text);
		TextView priceText = (TextView) row.findViewById(R.id.rr_price);
		TextView priceMinText = (TextView) row.findViewById(R.id.rr_price_min);
		TextView duration = (TextView) row.findViewById(R.id.rr_duration);
		TextView flightType = (TextView) row.findViewById(R.id.rr_flight_type);
		
		Itinerary singleItin = itinList.get(position);
		
		fromTime.setText(singleItin.getDepartDT().split(" ")[1]);
		toTime.setText(singleItin.getArrivDT().split(" ")[1]);
		priceText.setText("$" + singleItin.getCostString().split("\\.")[0]);
		priceMinText.setText(singleItin.getCostString().split("\\.")[1]);
		
		String hour = singleItin.getDurationString().split(":")[0];
		String minute = singleItin.getDurationString().split(":")[1];
		String time = hour + "h " + minute + "m";
		
		duration.setText(time);
		
		if (singleItin.getFlightType()) {
			flightType.setText("Non-stop");
		} else {
			flightType.setText("Connecting flights");
		}
		
		return row;
	}
	
	public void updateResults(List<Itinerary> results) {
		itinList = results;
		notifyDataSetChanged();
	}
}
