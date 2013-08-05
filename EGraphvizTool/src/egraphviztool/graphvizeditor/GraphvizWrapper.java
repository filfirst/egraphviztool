package egraphviztool.graphvizeditor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
	
	public Image runGraphviz(Display display) throws IOException, InterruptedException {
		Image image = null;
		
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(graphvizCmd_);
		BufferedInputStream stdInput = new BufferedInputStream(proc.getInputStream());
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		BufferedWriter stdOutput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
		stdOutput.write(graphvizData_);
		stdOutput.flush();
		stdOutput.close();
		image = new Image(display, stdInput);
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
