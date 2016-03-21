package it.tiwiz.rxjavacrunch.part7;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import it.tiwiz.rxjavacrunch.R;
import rx.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by connieli on 3/20/16.
 */
public class RxTogglesAdapter extends RecyclerView.Adapter<RxTogglesAdapter.ToggleViewHolder> {

	private List<String> mToggleItems = new ArrayList<>();

	private final OnToggleListener mListener;

	/**
	 * Instantiate with the toggle listener.
	 * @param listener
	 *      the activity
	 */
	public RxTogglesAdapter(final OnToggleListener listener) {
		mListener = listener;
	}

	public void setItems(final List<String> newItems) {
		mToggleItems.clear();
		if (newItems != null) {
			mToggleItems.addAll(newItems);
		}

		notifyDataSetChanged();
	}

	@Override
	public ToggleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.part7_toggle_row, parent, false);
		
		return new ToggleViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final RxTogglesAdapter.ToggleViewHolder holder, final int i) {
		holder.bind(mToggleItems.get(i), mListener);
	}

	@Override
	public int getItemCount() {
		return mToggleItems.size();
	}

	public static class ToggleViewHolder extends RecyclerView.ViewHolder {
		@Bind(R.id.tour_guide_name)
		TextView mNameView;

		@Bind(R.id.toggle_switch)
		SmartSwitch mSwitch;

		/**
		 * Track the data element
		 */
		@Nullable
		private String mToggleItem;

		private Subscription mSubscription;

		public ToggleViewHolder(final View itemView) {
			super(itemView);

			ButterKnife.bind(this, itemView);

		}

		public void bind(final String toggleItem, final OnToggleListener listener) {
			mNameView.setText(toggleItem);

			mToggleItem = toggleItem; // save model for listener

			RxCompoundButton
					.checkedChanges(mSwitch)
					.subscribe(isChecked -> {
						listener.onToggle(getToggleItem(), isChecked);
					});
		}

		@Nullable
		private String getToggleItem() {
			return mToggleItem;
		}
	}

}
