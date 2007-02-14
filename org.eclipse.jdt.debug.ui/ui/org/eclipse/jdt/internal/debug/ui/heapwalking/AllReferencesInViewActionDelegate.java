/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.debug.ui.heapwalking;

import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.debug.ui.IDebugView;
import org.eclipse.jdt.internal.debug.core.HeapWalkingManager;
import org.eclipse.jdt.internal.debug.core.JDIDebugPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Action delegate that turns on/off references being displayed as variables in the view.
 * 
 * @since 3.3
 */
public class AllReferencesInViewActionDelegate implements IPropertyChangeListener, IActionDelegate2, IViewActionDelegate {

	private IAction fAction;
	private IDebugView fView;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		HeapWalkingManager.getDefault().setShowReferenceInVarView(action.isChecked());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate2#init(org.eclipse.jface.action.IAction)
	 */
	public void init(IAction action) {
		fAction = action;
		action.setChecked(HeapWalkingManager.getDefault().isShowReferenceInVarView());
		JDIDebugPlugin.getDefault().getPluginPreferences().addPropertyChangeListener(this);
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		if (view instanceof IDebugView){
			fView = (IDebugView)view;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate2#dispose()
	 */
	public void dispose() {
		JDIDebugPlugin.getDefault().getPluginPreferences().removePropertyChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate2#runWithEvent(org.eclipse.jface.action.IAction, org.eclipse.swt.widgets.Event)
	 */
	public void runWithEvent(IAction action, Event event) {
		run(action);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.Preferences$IPropertyChangeListener#propertyChange(org.eclipse.core.runtime.Preferences.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (JDIDebugPlugin.PREF_SHOW_REFERENCES_IN_VAR_VIEW.equals(event.getProperty()) || JDIDebugPlugin.PREF_ALL_REFERENCES_MAX_COUNT.equals(event.getProperty())){
			if (fAction != null){
				fAction.setChecked(HeapWalkingManager.getDefault().isShowReferenceInVarView());
				fView.getViewer().refresh();
			}
		}
	}
}
