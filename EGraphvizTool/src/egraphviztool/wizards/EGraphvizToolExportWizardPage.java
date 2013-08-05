package egraphviztool.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class EGraphvizToolExportWizardPage extends WizardPage {
	private ISelection selection;
	
	public EGraphvizToolExportWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Multi-page Editor File");
		setDescription("This wizard creates a new file with *.gv extension that can be opened by a multi-page editor.");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite arg0) {
		// TODO Auto-generated method stub

	}

}
