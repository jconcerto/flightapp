package adapters;

import java.util.List;

import com.flightapp.androidproject.R;

import bookingApp.Flight;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter class for generating a single list of Itineraries to display.
 *
 */
public class DetailAdapter extends BaseAdapter{
	
	private Context context;
	private List<Flight> flightList;
	
    /**
     * Constructs a new DetailAdapter.
     * @param context The context of the needed activity.
     * @param resultList The list of flight results to display.
     */
    public DetailAdapter(Context context, List<Flight> resultList) {
    	this.context = context;
    	flightList = resultList;
    }
    
	@Override
	public int getCount() {
		return flightList.size();
	}

	@Override
	public Object getItem(int position) {
		return flightList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Create and set the TextView layouts inside xml file and generate
		// rows of ListView items.
		
		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.summary_row, parent, false);
		}
		else {
			row = convertView;
		}
		row.setClickable(false);
		
		Flight flightItem = flightList.get(position);
		
		TextView flightNum = (TextView) row.findViewById(R.id.summary_flight_num);
		flightNum.setText(flightItem.getFlightNumber());
		
		TextView airline = (TextView) row.findViewById(R.id.summary_airline);
		airline.setText(flightItem.getAirline());
		
		
		TextView price = (TextView) row.findViewById(R.id.summary_price);
		price.setText("$" + flightItem.getPriceString());
		
		
		TextView departDT = (TextView) row.findViewById(R.id.sum_depart_date);
		departDT.setText(flightItem.getDepartureDT());
		
		TextView arriveDT = (TextView) row.findViewById(R.id.sum_arrive_date);
		arriveDT.setText(flightItem.getArrivalDT());
		
		
		TextView fromLoc = (TextView) row.findViewById(R.id.summary_from);
		fromLoc.setText(flightItem.getOrigin());
		
		TextView toLoc = (TextView) row.findViewById(R.id.summary_to);
		toLoc.setText(flightItem.getDestination());
		
		TextView duration = (TextView) row.findViewById(R.id.summary_duration);
		String[] timeString = flightItem.getDurationString().split(":");
		duration.setText(timeString[0] + "h " + timeString[1] + "m");
		
		TextView numSeats = (TextView) row.findViewById(R.id.summary_num_seats);
		numSeats.setText(flightItem.getNumSeats() + " seat(s) left");
		
		
		return row;
	}
	
	/**
	 * Updates this adapter when a new list of flights is used.
	 * @param results The list of new results to use.
	 */
	public void updateResults(List<Flight> results) {
		flightList = results;
		notifyDataSetChanged();
	}
}
