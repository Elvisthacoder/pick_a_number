package listener;

/**
 * Created by taifa on 4/14/16.
 */
public interface OnValueChangeListener {


    /**
     * Invoked when the value changes on the {@link ActualNumberPicker}. Remember to set this listener to the View.
     *
     * @param oldValue The value picker had before the change
     * @param newValue Tha value picker has now, after the change
     */
    void onValueChanged(int oldValue, int newValue);
}
