package net.youmi.ads.base.template;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhitao
 * @since 2017-04-14 16:29
 */
public abstract class TListenerManager<T> {
	
	private List<T> mListeners;
	
	protected synchronized List<T> getListeners() {
		if (mListeners == null) {
			mListeners = new ArrayList<T>();
		}
		return mListeners;
	}
	
	public boolean addListener(T listener) {
		if (listener != null) {
			final List<T> list = getListeners();
			if (!list.contains(listener)) {
				return list.add(listener);
			} else {
				return true;
			}
		}
		return false;
	}
	
	public boolean removeListener(T listener) {
		if (listener != null) {
			final List<T> list = getListeners();
			return list.remove(listener);
		}
		return false;
	}
	
	public boolean isEmpty() {
		if (mListeners == null) {
			return true;
		}
		return mListeners.isEmpty();
	}
	
}