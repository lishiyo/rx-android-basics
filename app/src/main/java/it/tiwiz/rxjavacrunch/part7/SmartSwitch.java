package it.tiwiz.rxjavacrunch.part7;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by connieli on 3/20/16.
 */
public class SmartSwitch extends Switch {

	private CompoundButton.OnCheckedChangeListener mListener;

	/**
	 * Constructor.
	 *
	 * @param context
	 *      the context.
	 */
	public SmartSwitch(final Context context) {
		super(context);
	}

	/**
	 * Constructor.
	 *
	 * @param context
	 *      the context.
	 * @param attrs
	 *      the {@link AttributeSet}.
	 */
	public SmartSwitch(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructor.
	 *
	 * @param context
	 *      the context.
	 * @param attrs
	 *      the {@link AttributeSet}.
	 * @param defStyle
	 *      the style.
	 */
	public SmartSwitch(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Register a callback to be invoked when the checked state of this button
	 * changes.
	 *
	 * @param listener the callback to call on checked state change
	 */
	@Override
	public void setOnCheckedChangeListener(final OnCheckedChangeListener listener) {
		mListener = listener;
		super.setOnCheckedChangeListener(mListener);
	}

	/**
	 * calls {@link #setChecked(boolean)} without calling onCheckedChanged
	 *
	 * @param checked
	 *      true to check the button, false to uncheck it
	 */
	public void silentlySetChecked(final boolean checked) {
		toggleListener(false);
		setChecked(checked);
		toggleListener(true);
	}

	/**
	 * Enables or disables the OnCheckedChangeListener for this switch
	 *
	 * @param enabled
	 *      true to enable the listener, false to disable it
	 */
	private void toggleListener(final boolean enabled) {
		if (enabled) {
			super.setOnCheckedChangeListener(mListener);
		} else {
			super.setOnCheckedChangeListener(null);
		}
	}
}
