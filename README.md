# rcp-update
This repo is to show how to work with RCP automatic update.

#1 Plugin Project
##1.1 Create a plugin project called **xdemo**

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476763224965_34.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476763249332_18.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476763292688_88.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476763305103_57.png)

##1.2 Add p2 plugins to the dependencies
  - org.eclipse.equinox.p2.core
  - org.eclipse.equinox.p2.engine
  - org.eclipse.equinox.p2.operations
  - org.eclipse.equinox.p2.metadata.repository

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476764022214_10.png)

##1.3 Add automatic update function to this plugin
###1.3.1 Create a Model class
```Java
package xdemo.model;

public class RepoLocation {
	public static final RepoLocation REPO_LOCATION = new RepoLocation();
	
	private String loc = "file:////C:/Repo";

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}
	
}
```
###1.3.2 Create **UpdateHandler.class** with following content, this is the handler for automatic update
```Java
package xdemo.handlers;

import static xdemo.model.RepoLocation.REPO_LOCATION;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class UpdateHandler {

	@Execute
	public void execute(final IProvisioningAgent agent, final Shell shell, final UISynchronize sync,
			final IWorkbench workbench) {
		Job updateJob = new Job("Update Job") {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				return checkForUpdates(agent, shell, sync, workbench, monitor);
			}
		};
		updateJob.schedule();
	}

	private IStatus checkForUpdates(final IProvisioningAgent agent, final Shell shell, final UISynchronize sync,
			final IWorkbench workbench, IProgressMonitor monitor) {

		// configure update operation
		final ProvisioningSession session = new ProvisioningSession(agent);
		final UpdateOperation operation = new UpdateOperation(session);
		configureUpdate(operation);

		// check for updates, this causes I/O
		final IStatus status = operation.resolveModal(monitor);

		// failed to find updates (inform user and exit)
		if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			showMessage(shell, sync);
			return Status.CANCEL_STATUS;
		}

		// run installation
		final ProvisioningJob provisioningJob = operation.getProvisioningJob(monitor);

		// updates cannot run from within Eclipse IDE!!!
		if (provisioningJob == null) {
			System.err.println("Trying to update from the Eclipse IDE? This won't work!");
			return Status.CANCEL_STATUS;
		}
		configureProvisioningJob(provisioningJob, shell, sync, workbench);

		provisioningJob.schedule();
		return Status.OK_STATUS;

	}

	private void configureProvisioningJob(ProvisioningJob provisioningJob, final Shell shell, final UISynchronize sync,
			final IWorkbench workbench) {

		// register a job change listener to track
		// installation progress and notify user upon success
		provisioningJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK()) {
					sync.syncExec(new Runnable() {

						@Override
						public void run() {
							boolean restart = MessageDialog.openQuestion(shell, "Updates installed, restart?",
									"Updates have been installed. Do you want to restart?");
							if (restart) {
								workbench.restart();
							}
						}
					});

				}
				super.done(event);
			}
		});

	}

	private void showMessage(final Shell parent, final UISynchronize sync) {
		sync.syncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog.openWarning(parent, "No update", "No updates for the current installation have been found. Loc="+REPO_LOCATION.getLoc());
			}
		});
	}

	private UpdateOperation configureUpdate(final UpdateOperation operation) {
		// create uri and check for validity
		URI uri = null;
		try {
			uri = new URI(REPO_LOCATION.getLoc());
		} catch (final URISyntaxException e) {
			System.err.println(e.getMessage());
			return null;
		}

		// set location of artifact and metadata repo
		operation.getProvisioningContext().setArtifactRepositories(new URI[] { uri });
		operation.getProvisioningContext().setMetadataRepositories(new URI[] { uri });
		return operation;
	}

}
```
###1.3.3 Modify UI code

Add an update command

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765113081_12.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765139744_30.png)

Add an update handler

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765158120_17.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765196343_27.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765216168_99.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765250799_31.png)

Add an update menu

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765278824_72.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765310711_25.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765324464_43.png)

###1.3.4 After all changes, we run our rcp application

We have to change run configure as following

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765523543_41.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765539198_96.png)

Now we should be able to see the UI, and we could save the text and press **Update** Menu. However, the update function won't work, you should see this in eclipse console - *Trying to update from the Eclipse IDE? This won't work!*

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765563940_23.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765586020_16.png)

#2. Feature Project
Create Feature Project is pretty straightforward.

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765757995_66.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765765355_48.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765780235_95.png)

#3. Product Project
At last, we need to create product project, this project will contain nothing but *.product* file. we could just move the *.product* file from the plugin project.

##3.1 Create a normal project

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476766003689_54.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476766010569_26.png)

##3.2 Move the *.product* file from plugin project to product project

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476766245425_1.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476766255775_88.png)

##3.3 Change the *.product*
Add ID to the file and change the *Product Definition* from *plugins* to *features*
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476766309379_7.png)
Add features to *Contents* Tab
1. Add following features
	- xdemo.feature
	- org.eclipse.equinox.p2.core.feature
	- org.eclipse.e4.rcp
1. After add above features just press *Add Required* to add dependent features
1. Now we should have 11 features totally

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476766358099_94.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476766428922_95.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476766437154_63.png)

##3.4 Now we run the application from current product file
We should be able to see the same GUI as before

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476766507446_20.png)

#4 Update in Action
##4.1 We export/build our application, let's name it v1.

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476771001281_20.png)

##4.2 Now we make a change to application, like to add an new item

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476771045966_74.png)

##4.3 We export/build our application again in a different location, also we name it v2.

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476771077010_48.png)

##4.4 Now we run v1 application, it's in the v1 export location's sub folder *eclipse*
1. The application should start, and we edit the text, which is the location of update repository path.
1. We edit the location to v1 repository, remember that we have to change "\" to "/".
1. Now we press *Update* menu. you should see a prompt that tells us there is no updates found, which is expected.
1. Now we dit the location to v2 repository, and press *Update* menu. Now updates should be start right away and prompt us to restart application when it's done

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476771172262_87.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476771187869_47.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476771204206_55.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476771215199_79.png)








