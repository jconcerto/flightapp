package adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flightapp.androidproject.R;

import bookingApp.Flight;
import bookingApp.Itinerary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * An adapter class for displaying multiple Itineraries which contain
 * Flight information.
 *
 */
public class DetailExpandAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private Map<Itinerary, Integer> itinMap;
	private ArrayList<Itinerary> itinArrayList;
	
	/**
	 * Constructs a new DetailExpandAdapter.
	 * @param context The context of the needed activity to display the list.
	 * @param itinList The List of Itineraries to display.
	 * @param itinMap A map of Itineraries and the number of seats to book.
	 */
	public DetailExpandAdapter(Context context, ArrayList<Itinerary> itinList, 
			Map<Itinerary, Integer> itinMap) {
		this.context = context;
		itinArrayList = itinList;
		this.itinMap = itinMap;
		
	}

	@Override
	public int getGroupCount() {
		return itinArrayList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return itinArrayList.get(groupPosition).getFlightArray().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return itinArrayList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return itinArrayList.get(groupPosition).getFlightArray().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {


		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.summary_row_title, parent, false);
		}
		else {
			row = convertView;
		}
		
		Itinerary titleItin = itinArrayList.get(groupPosition);
		
		TextView itinNumber = (TextView) row.findViewById(R.id.srt_itin_num);
		itinNumber.setText("Itinerary " + (groupPosition + 1));
		
		TextView fromTo = (TextView) row.findViewById(R.id.srt_from_to);
		fromTo.setText(titleItin.getOrigin() + " to " + titleItin.getDestination());
		
		TextView numPerson = (TextView) row.findViewById(R.id.srt_num_person);
		int numSeats = itinMap.get(titleItin);
		numPerson.setText(numSeats + " Person(s)");
		
		TextView totalPrice = (TextView) row.findViewById(R.id.srt_total_price);
		double priceTotal = 0;
		for (int i = 0; i < numSeats; i++) {
			priceTotal += titleItin.getTotalCost();
		}
		totalPrice.setText("$" + String.format("%.2f", priceTotal));
		
		return row;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.summary_row, parent, false);
		}
		else {
			row = convertView;
		}
		
		Flight flightItem = itinArrayList.get(groupPosition).
				getFlightArray().get(childPosition);
		
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

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
