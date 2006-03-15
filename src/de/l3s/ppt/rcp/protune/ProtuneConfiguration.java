package de.l3s.ppt.rcp.protune;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
//import org.eclipse.jface.text.reconciler.MonoReconciler;
//import org.eclipse.jface.text.reconciler.IReconciler;
//import org.eclipse.core.runtime.NullProgressMonitor;

public class ProtuneConfiguration extends SourceViewerConfiguration {
//	private static final int DEFAULT_DELAY = 500;
	private ProtuneColorManager colorManager;

	public ProtuneConfiguration(ProtuneColorManager colorManager) {
		this.colorManager = colorManager;
	}
	
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE };
	}
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		ProtuneRepairer repairer = new ProtuneRepairer(colorManager);
		reconciler.setDamager(repairer, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(repairer, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}
//	public IReconciler getReconciler(ISourceViewer sourceViewer) {
//		ProtuneReconcilingStrategy strategy = new ProtuneReconcilingStrategy(fEditor);
//		MonoReconciler reconciler = new MonoReconciler(strategy, false);
//		reconciler.setProgressMonitor(new NullProgressMonitor());
//		reconciler.setDelay(DEFAULT_DELAY);
//
//		return reconciler;
//	}	
}
