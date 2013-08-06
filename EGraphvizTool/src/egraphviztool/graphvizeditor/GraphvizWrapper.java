package egraphviztool.graphvizeditor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import egraphviztool.Activator;
import egraphviztool.preferences.PreferenceConstants;

public class GraphvizWrapper implements IPropertyChangeListener {
	private String graphvizData_ = null;
	private String graphvizCmd_ = null;
	
	public GraphvizWrapper() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String graphvizDir = store.getString(PreferenceConstants.P_PATH);
		genGraphvizCommand(graphvizDir);
		store.addPropertyChangeListener(this);
		System.out.println(graphvizCmd_);
	}
	
	public void setGraphvizData(String graphvizData) {
		graphvizData_ = graphvizData;
	}
	
	public String getGraphvizData() {
		return graphvizData_;
	}
	
	public Image runGraphviz(Display display) throws IOException,
			InterruptedException, CoreException {
		Image image = null;
		
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(graphvizCmd_);
		BufferedInputStream stdInput = new BufferedInputStream(proc.getInputStream());
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		BufferedWriter stdOutput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
		stdOutput.write(graphvizData_);
		stdOutput.flush();
		stdOutput.close();
		
		try {
			image = new Image(display, stdInput);
		} catch (SWTException e) {
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = stdError.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
		
			if (builder.toString().length() > 0) {
				IStatus status = new Status(IStatus.ERROR, "EGraphvizTool",
						builder.toString());
				throw new CoreException(status);
			}
		}
		
		stdError.close();
		proc.waitFor();
		
		return image;
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		System.out.println(propertyChangeEvent.getProperty());
		if (propertyChangeEvent.getProperty().equals(PreferenceConstants.P_PATH)) {
			String graphvizDir = (String)propertyChangeEvent.getNewValue();
			genGraphvizCommand(graphvizDir);
			System.out.println(graphvizCmd_);
		}
	}
	
	private void genGraphvizCommand(String graphvizDir) {
		StringBuilder cmdSb = new StringBuilder();
		cmdSb.append(graphvizDir);
		cmdSb.append("/dot -Tpng");
		graphvizCmd_ = cmdSb.toString();
	}
}
