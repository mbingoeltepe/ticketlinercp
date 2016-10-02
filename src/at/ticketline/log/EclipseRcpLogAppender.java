package at.ticketline.log;

import java.io.IOException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class EclipseRcpLogAppender extends AppenderSkeleton {

	public static ILog logger = null;
	public static String pluginId = null;

	private boolean init = false;

	private boolean consoleView = false;
	private MessageConsole console = null;
	private MessageConsoleStream consoleStream = null;

	protected void init() {
		this.console = new MessageConsole("Log Console", null);

		this.consoleStream = console.newMessageStream();
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[] { this.console });
		this.init = true;
	}

	@Override
	protected void append(LoggingEvent evt) {
		if (evt.getLevel() == Level.OFF) {
			return;
		}
		String msg = this.layout.format(evt);

		if (this.consoleView) {
			if ((this.init == false) && (PlatformUI.isWorkbenchRunning())) {
				this.init();
			}
			if (this.init) {
				try {
					this.consoleStream.print(msg);
				} catch (Exception e) {
					IStatus es = new Status(IStatus.ERROR,
							EclipseRcpLogAppender.pluginId,
							"Exception writing log message to Log Console", e);
					EclipseRcpLogAppender.logger.log(es);
				}
			}
		}

		if (EclipseRcpLogAppender.logger == null) {
			return;
		}
		IStatus status = null;
		int level = IStatus.OK;
		Level l = evt.getLevel();
		if ((l == Level.FATAL) || (l == Level.ERROR)) {
			level = IStatus.ERROR;
		} else if (l == Level.WARN) {
			level = IStatus.WARNING;
		} else if ((l == Level.DEBUG) || (l == Level.INFO)) {
			level = IStatus.INFO;
		}

		ThrowableInformation ti = evt.getThrowableInformation();
		if (ti != null) {
			status = new Status(level, EclipseRcpLogAppender.pluginId, msg, ti
					.getThrowable());
		} else {
			status = new Status(level, EclipseRcpLogAppender.pluginId, msg);
		}

		EclipseRcpLogAppender.logger.log(status);
	}

	@Override
	public void close() {
		try {
			this.consoleStream.close();
		} catch (IOException e) {
			/* Ignore */
		}
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	public boolean isConsoleView() {
		return consoleView;
	}

	public void setConsoleView(boolean consoleView) {
		this.consoleView = consoleView;
	}

}
