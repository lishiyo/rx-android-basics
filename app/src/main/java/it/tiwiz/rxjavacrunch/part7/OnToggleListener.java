package it.tiwiz.rxjavacrunch.part7;

/**
 * Created by connieli on 3/20/16.
 */

public interface OnToggleListener {
	/**
	 *
	 * @param toggleName
	 *      the data element backing the viewholder
	 * @param toggleOn
	 *      whether toggled on or off
	 */
	void onToggle(String toggleName, final boolean toggleOn);
}
