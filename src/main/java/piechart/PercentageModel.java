package piechart;

import java.util.Set;

/**
 * This class is a MVC "Model" of a percentage (a value such as 0 <= x <= 1)
 **/
public class PercentageModel extends Percentage {
	// Les vues enregistrées dans le modèle
	private Set<PercentageView> myViews = new java.util.HashSet<PercentageView> ();

	/**
	 * Create a Percentage with an initial value
	 * @param initialValue : the initial value
	 * @throws IllegalArgumentException if value is not correct
	 **/
	public PercentageModel(float initialValue) {
		super(initialValue);
	}

	/**
	 * Create a Percentage with an initial value of 0
	 **/
	public PercentageModel() {
		this(0.0F);
	}

	/**
	 * Change the value of this Percentage.
	 * Notify Listeners of this model.
	 * @param newValue : the value
	 * @throws IllegalArgumentException if value is not correct
	 **/
	@Override
	public void setValue(float newValue) {
		float oldValue = getValue(); // Mémoriser l'ancienne valeur
		super.setValue(newValue); // Vérifie la validité du paramètre
		if (getValue() != oldValue) 
			// Percentage has changed, notify the Views.
			notifyViews();
	}

	/**
	 * Add a new view to this model
	 * @param l     the new view
	 **/
	public void addView(PercentageView l) {
		myViews.add(l);
		// Bring the view up to date
		l.notify(this);
	}

	/**
	 * Remove a view from this model
	 * @param l     the  view to remove
	 **/
	public void removeView(PercentageView l) {
		myViews.remove(l);
	}

	/**
	 * Iterates on all views and
	 * send the notify method to each
	 */
	protected void notifyViews() {
		if (null != myViews) 
			for (PercentageView view : myViews) 
				view.notify(this);
	}
}
