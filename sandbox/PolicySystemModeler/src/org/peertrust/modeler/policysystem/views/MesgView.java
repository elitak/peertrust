package org.peertrust.modeler.policysystem.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.Log4JLogRecord;
import org.apache.log4j.lf5.LogLevel;
import org.apache.log4j.lf5.LogLevelFormatException;
import org.apache.log4j.lf5.LogRecord;
import org.apache.log4j.lf5.LogRecordFilter;
import org.apache.log4j.lf5.viewer.FilteredLogTableModel;
import org.apache.log4j.lf5.viewer.LogTable;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.peertrust.modeler.policysystem.PolicysystemPlugin;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.ResourcePolicyContentProvider;



public class MesgView extends ViewPart 
{
	
	
	static final public String ID="org.peertrust.modeler.policysystem.MesgView";	
	
	private LogTableAppender logAppender;
	
	private ToolBarManager toolbarManager;
	private Action addAction;
	private Shell shell;
	
	private AbstractAction clearAction;
	public MesgView() {
		super();
	}

	public void createPartControl(Composite parent) {
		logAppender= new LogTableAppender(parent);
		logAppender.registerAt(Logger.getRootLogger());
		makeToolBarActions();
		TreeViewer tv;
		makeListselectionListener(parent);
		shell=parent.getShell();
//		MenuBarCreator.createMenubar(
//				//getViewSite().getPart(),
//				this.getViewSite(),
//				(IDoubleClickListener)null,
//				new Action[]{addAction,removeAction},
//				null,
//				parent.getParent(),//null,//treeView.getControl(),
//				selProvider,//null,//treeView,
//				"");
		
	}

	public void setFocus() {
		
	}

	private void makeToolBarActions() 
	{
		;
		///
		addAction = new Action() {
			public void run() {
			}
		};
		addAction.setText("create");
		addAction.setToolTipText("create");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		
		
//		removeAction = new Action() {
//			public void run() {
//				
//			}
//		};
//		
//		removeAction.setText("remove");
//		removeAction.setToolTipText("remove");
//		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
//				getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
	}
	////////////////////////////////////////////////////////////
	////////////////APPENDER////////////////////////////////////
	////////////////////////////////////////////////////////////
	class LogTableAppender extends AppenderSkeleton
	{
		private LogTable logTable;
		private JTextArea ltTextArea;
		private Action removeAction;
		private JToolBar jToolBar;
		AppenderLogRecordFilter logRecordFilter;
		
		public LogTableAppender(Composite parent)
		{
			init(parent);
		}
		
		private  final void init(Composite parent)
		{
			ltTextArea = new JTextArea();
			logTable = new LogTable(ltTextArea);
			LogRecord lr= new Log4JLogRecord();
			lr.setMessage("dummy mesg");
			lr.setLevel(LogLevel.INFO);
			logTable.getColumnModel().getColumn(0).setResizable(true);
			logTable.getColumnModel().getColumn(1).setResizable(true);
			logTable.getColumnModel().getColumn(2).setResizable(true);
			logTable.getFilteredLogTableModel().addLogRecord(lr);
			///
			Composite composite= 
				new Composite(
						parent,
						SWT.EMBEDDED);
			
			composite.setLayout(new FillLayout());	
			Frame frame=SWT_AWT.new_Frame(composite);
			
			JPanel panel=new JPanel();
			panel.setLayout(new BorderLayout());
			
			JScrollPane sp= 
				new JScrollPane();
			sp.add(logTable);			
			sp.setViewportView(logTable);
			sp.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			logTable.setBorder(BorderFactory.createLineBorder(Color.GREEN));
			
			frame.add(panel);
			panel.add(sp,BorderLayout.CENTER);
			makeToolBar();
			panel.add(jToolBar,BorderLayout.NORTH);
			
			makeLogLevelFilter(logTable);
			
		}
		
		private void makeLogLevelFilter(LogTable table)
		{
			logRecordFilter = new AppenderLogRecordFilter(table);
			logTable.getFilteredLogTableModel().setLogRecordFilter(logRecordFilter);
			logTable.getFilteredLogTableModel().refresh();
		}
		
		private void makeToolBar()
		{
			///clear action
			clearAction= new AbstractAction()
			{

				public void actionPerformed(ActionEvent e) {
					logTable.getFilteredLogTableModel().clear();
				}
				
			};
			JButton clearButton=new JButton(clearAction);
			clearButton.setText("C");
			///level chosser
			AbstractAction comboAction= new AbstractAction()
			{

				public void actionPerformed(ActionEvent e) {
					///////////////
					Object source=e.getSource();
					System.out.println("\nSource:"+e.getSource());
					if(source instanceof JComboBox)
					{
						Object sel=((JComboBox)source).getSelectedItem();
						if(sel instanceof String)
						{
							try {
								LogLevel logLevel= LogLevel.valueOf((String)sel);
								//logRecordFilter
								logRecordFilter.setLevel(logLevel);
								//logTable.getFilteredLogTableModel().setLogRecordFilter(logRecordFilter);
							} catch (LogLevelFormatException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				
			};
			LogRecord lr;
			//List levels=logTable.getFilteredLogTableModel().Record.
			String[] levels=new String[]{
					LogLevel.DEBUG.getLabel(),
					LogLevel.INFO.getLabel(),
					LogLevel.WARN.getLabel(),
					LogLevel.ERROR.getLabel(),
				};
			JComboBox combo= 
				new JComboBox(levels);
			combo.setAction(comboAction);
			//Dimension cDim=combo.getPreferredSize();
			//System.out.println("Dim:"+cDim);
			//cDim.width=70;
			//cDim.height=24;
			//combo.setSize(cDim);
			//combo.setMaximumSize(cDim);
			///toolbar
			jToolBar= new JToolBar();			
			jToolBar.add(clearButton);
			jToolBar.add(combo);
			
			//tb.add(removeAction);
		}
		protected void append(LoggingEvent event) 
		{
	        String category = event.getLoggerName();
	        String logMessage = event.getRenderedMessage();
            String nestedDiagnosticContext = event.getNDC();
            String threadDescription = event.getThreadName();
            String level = event.getLevel().toString();
            long time = event.timeStamp;
            LocationInfo locationInfo = event.getLocationInformation();
            final Log4JLogRecord record = new Log4JLogRecord();
            record.setCategory(category);
            record.setMessage(logMessage);
            record.setLocation(locationInfo.fullInfo);
            record.setMillis(time);
            record.setThreadDescription(threadDescription);
            if (nestedDiagnosticContext != null) record.setNDC(nestedDiagnosticContext); else record.setNDC("");
            if (event.getThrowableInformation() != null) record.setThrownStackTrace(event.getThrowableInformation());
            try {
            	record.setLevel(LogLevel.valueOf(level));
            } catch  (LogLevelFormatException e) {
            	record.setLevel(LogLevel.WARN);
            }
            SwingUtilities.invokeLater(new Runnable(){
            public void run() 
            {
            	FilteredLogTableModel tm;
            	logTable.getFilteredLogTableModel().addLogRecord(record);
            	//logTable.getFilteredLogTableModel().
            }
            });
		}

		public void close() 
		{
			
		}

		public boolean requiresLayout() {
			return false;
		}
		
		
		void registerAt(Logger logger)
		{
			logger.addAppender(this);
		}
		
		void deleteSelection()
		{
			logTable.getModel();
		}
		
	}
	///////////////////////////////////////////////////////////
	void makeListselectionListener(final Composite parent)
	{
		ListSelectionListener lsl=
			new ListSelectionListener()
			{
				Shell theShell=
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				//Object o= PolicysystemPlugin.getDefault().
				public void valueChanged(ListSelectionEvent e) {
					int selInt=e.getFirstIndex();
					logAppender.logTable.getSelectedRow();
					FilteredLogTableModel model=
						logAppender.logTable.getFilteredLogTableModel();
					int cCount=model.getColumnCount();
					StringBuffer strBuffer=
								new StringBuffer(512);
					for(int c=0;c<cCount;c++)
					{
						strBuffer.append(model.getValueAt(0,c));
						strBuffer.append(":\n");
						strBuffer.append(model.getValueAt(selInt,c));
					}
					
					
					///////////
					PolicysystemPlugin.getDefault().showMessage(strBuffer.toString());
										
				}
			
			};
			
			
//			logAppender.logTable.getSelectionModel(
//								).addListSelectionListener(lsl);
			////
			
			MouseListener ml= new MouseListener()
			{

				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount()>1)
					{
						//int selInt=e.getFirstIndex();
						int selInt=logAppender.logTable.getSelectedRow();
						FilteredLogTableModel model=
							logAppender.logTable.getFilteredLogTableModel();
						int cCount=model.getColumnCount();
						StringBuffer strBuffer=
									new StringBuffer(512);
						
						for(int c=0;c<cCount;c++)
						{
							strBuffer.append("\n==========\n");
							strBuffer.append(logAppender.logTable.getColumnName(c));
							strBuffer.append(":\n");
							strBuffer.append(model.getValueAt(selInt,c));
						}
						
						
						///////////
						PolicysystemPlugin.getDefault().showMessage(strBuffer.toString());
					}
					
				}

				public void mousePressed(MouseEvent e) {
					
				}

				public void mouseReleased(MouseEvent e) {
					
				}

				public void mouseEntered(MouseEvent e) {
					
				}

				public void mouseExited(MouseEvent e) {
					
				}
				
			};
			
			logAppender.logTable.addMouseListener(ml);
	}
	///////////////////////////////////////////////////////////
	class AppenderLogRecordFilter implements LogRecordFilter
	{
		private LogLevel logLevel;
		private LogTable logTable;
		
		public AppenderLogRecordFilter(LogTable logTable)
		{
			this.logTable=logTable;
			logLevel=LogLevel.WARN;
		}
		
		public void setLevel(LogLevel logLevel)
		{
			this.logLevel=logLevel;
			if(this.logTable!=null)
			{
				//this.logTable.getFilteredLogTableModel().fireTableDataChanged();
				this.logTable.getFilteredLogTableModel().refresh();
			}
		}
		public boolean passes(LogRecord lr) {
			if(lr==null || logLevel==null)
			{
				return true;
			}
			
			return  logLevel.encompasses(lr.getLevel());
			//return false;
		}

			
	}
	
	///////////////////////////////////////////////////////////

	
	//////////////////////////////////////////////////////////
	class ErrorLogTable implements ILogListener
	{

		private TableViewer logTable;
		///////////Iloglistener intergface
		public void logging(IStatus status, String plugin) {
			
		}
		
		////////
		private void makeTable()
		{
			//TableViewer tv= new TableViewer();
			//Table t; t.set
			ResourcePolicyContentProvider provider=
					new ResourcePolicyContentProvider();
			logTable.setContentProvider(provider);
			logTable.setLabelProvider(provider);
			TableLayout layout= new TableLayout();
			layout.addColumnData(new ColumnWeightData(33,true));
			layout.addColumnData(new ColumnWeightData(33,true));
			layout.addColumnData(new ColumnWeightData(34,true));
			
			Table table=logTable.getTable();
			table.setLayout(layout);
			TableColumn nameC=
					new TableColumn(
							table,
							SWT.CENTER,
							0);
			nameC.setText(PolicySystemRDFModel.LNAME_PROP_HAS_NAME);
			
			TableColumn valueC=
				new TableColumn(
						table,
						SWT.CENTER,
						1);
			valueC.setText(PolicySystemRDFModel.LNAME_PROP_HAS_VALUE);
			
			TableColumn filterC=
				new TableColumn(
						table,
						SWT.CENTER,
						2);
			filterC.setText(PolicySystemRDFModel.LNAME_PROP_HAS_FILTER);
			
			table.setLinesVisible(true);
			table.setHeaderVisible(true);		
			logTable.setInput(PolicySystemRDFModel.LNAME_PROP_HAS_NAME);
			
		}
		
	}
	
	ISelectionProvider selProvider=
		new ISelectionProvider()
		{

			public void addSelectionChangedListener(ISelectionChangedListener listener) {
				// TODO Auto-generated method stub
				
			}

			public ISelection getSelection() {
				// TODO Auto-generated method stub
				return null;
			}

			public void removeSelectionChangedListener(ISelectionChangedListener listener) {
				// TODO Auto-generated method stub
				
			}

			public void setSelection(ISelection selection) {
				// TODO Auto-generated method stub
				
			}
		
		};
		
		

}
