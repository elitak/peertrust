package test.org.protune;

import org.protune.core.ActionWellPerformed;
import org.protune.core.ActionWrongPerformed;
import org.protune.core.Check;
import org.protune.core.Checker;
import org.protune.core.Notification;
import org.protune.core.UnknownNotificationException;

/**
 * Dummy implementation of the interface {@link org.protune.core.Checker}, which is unable to handle any
 * notification type.
 * @author jldecoi
 */
public class DummyChecker implements Checker {

	public Check checkNotification(ActionWellPerformed awp) throws UnknownNotificationException{
		throw new UnknownNotificationException();
	}

	public Check checkNotification(ActionWrongPerformed awp) throws UnknownNotificationException{
		throw new UnknownNotificationException();
	}

	public Check checkNotification(Notification n) throws UnknownNotificationException{
		throw new UnknownNotificationException();
	}

}
