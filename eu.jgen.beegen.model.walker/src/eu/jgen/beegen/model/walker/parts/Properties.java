/**
 * [The "BSD license"]
 * Copyright (c) 2016,JGen Notes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions 
 *    and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS 
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package eu.jgen.beegen.model.walker.parts;

import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import eu.jgen.beegen.model.api.JGenObject;
import eu.jgen.beegen.model.meta.MetaHelper;
import eu.jgen.beegen.model.meta.ObjMetaType;
import eu.jgen.beegen.model.meta.PrpMetaType;

public class Properties {

	private static final String LABEL_ORDER = "Order";

	private static final String LABEL_OPTIONALITY = "Optionality";

	private static final String LABEL_INVERSE_TYPE_CODE = "Inverse Type Code";

	private static final String LABEL_INVERSE_TYPE_MNEMONIC = "Inverse Type Mnemonic";

	private static final String LABEL_CARDINALITY = "Cardinality";

	private static final String LABEL_ASSOCIATION_TYPE_CODE = "Association Type Code";

	private static final String LABEL_DESTINATION_OBJECT = "Destination Object:";

	private static final String LABEL_ASSOCIATION = "Association:";

	private static final String LABEL_SOURCE_OBJECT = "Source Object:";

	private static final String LABEL_PROPERTIES = "Properties:";

	private static final String LABEL_OBJECT_TYPE_CODE = "Object Type Code";

	private static final String LABEL_NAME = "Name";

	private static final String LABEL_MNEMONIC = "Mnemonic";

	private static final String LABEL_ID = "Id";

	private static final String LABEL_OBJECT = "Object:";

	private static final String INDENT = "     ";

	private final Image OBJECT = getImage("objectattr.gif");
	private final Image FORWARD = getImage("forward.gif");
	private final Image BACKWARD = getImage("backward.gif");
	private final Image PROPERTY = getImage("property.gif");

	class Row {

		private String property;
		private String value;
		private Image image;

		public Row(String description, String value, Image image) {
			super();
			this.property = description;
			this.value = value;
			this.image = image;
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String description) {
			this.property = description;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Image getImage() {
			return image;
		}

		public void setImage(Image image) {
			this.image = image;
		}
	}

	private List<Row> rows = new Vector<Row>();

	private Table table;
	private TableViewer tableViewer;

	@Inject
	public Properties() {
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(parent, SWT.BORDER);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));

		Group grpPropertiesOfThe = new Group(composite, SWT.NONE);
		grpPropertiesOfThe.setToolTipText(
				"This view displays details for the object currently selected \r\nfrom the model expansion tree. \r\n\r\nIt shows the following information:\r\n\t- details of the association with the parent,\r\n\t- details of the object, \r\n\t- list of properties.");
		grpPropertiesOfThe.setFont(SWTResourceManager.getFont("Cambria", 12, SWT.BOLD));
		GridData gd_grpPropertiesOfThe = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpPropertiesOfThe.widthHint = 435;
		grpPropertiesOfThe.setLayoutData(gd_grpPropertiesOfThe);
		grpPropertiesOfThe.setText("Details and Properties of the selected object");
		grpPropertiesOfThe.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite compositeScroll = new Composite(grpPropertiesOfThe, SWT.BORDER);
		compositeScroll.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite compositeTable = new Composite(compositeScroll, SWT.NONE);
		TableColumnLayout tcl_compositeTable = new TableColumnLayout();
		compositeTable.setLayout(tcl_compositeTable);

		tableViewer = new TableViewer(compositeTable, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setColumnProperties(new String[] {});
		tableViewer.setContentProvider(new ArrayContentProvider());
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn tableViewerColumnDesc = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnDescription = tableViewerColumnDesc.getColumn();
		tcl_compositeTable.setColumnData(tblclmnDescription, new ColumnPixelData(150, true, true));
		tblclmnDescription.setText("Description");
		tableViewerColumnDesc.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if (element instanceof Row) {
					Row row = (Row) element;
					return row.getProperty();
				}
				return super.getText(element);
			}
		});

		TableViewerColumn tableViewerColumnValue = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnValue = tableViewerColumnValue.getColumn();
		tcl_compositeTable.setColumnData(tblclmnValue, new ColumnPixelData(252, true, true));
		tblclmnValue.setText("Value");
		tableViewerColumnValue.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if (element instanceof Row) {
					Row row = (Row) element;
					return row.getValue();
				}
				return super.getText(element);
			}

			@Override
			public Image getImage(Object element) {
				if (element instanceof Row) {
					Row row = (Row) element;
					return row.getImage();
				}
				return super.getImage(element);
			}
		});

	}

	@Inject
	void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) JGenObject genObject) {
		if (genObject != null) {
			rows.clear();
			rows.add(new Row(LABEL_OBJECT, "", null));
			rows.add(new Row(INDENT + LABEL_ID, String.valueOf(genObject.getId()), OBJECT));
			rows.add(new Row(INDENT + LABEL_MNEMONIC, genObject.getObjMetaType().name(), OBJECT));
			String name = genObject.findTextProperty(PrpMetaType.NAME);
			if (name.length() > 0) {
				rows.add(new Row(INDENT + LABEL_NAME, name, OBJECT));
			}
			rows.add(new Row(INDENT + LABEL_OBJECT_TYPE_CODE, String.valueOf(genObject.getObjMetaType().code), OBJECT));
			addObjectProperies(genObject);
			tableViewer.setInput(rows);
		}
	}

	@Inject
	void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) AssociationNodeOne associationNodeOne) {
		if (associationNodeOne != null) {
			MetaHelper meta = associationNodeOne.getFromGenObject().genContainer.meta;
			ObjMetaType objTypeCode = associationNodeOne.getFromGenObject().getObjMetaType();
			Image image = meta.isAssociationForward(objTypeCode, associationNodeOne.getAscMetaType()) ? FORWARD
					: BACKWARD;
			rows.clear();
			// source
			rows.add(new Row(LABEL_SOURCE_OBJECT, "", null));
			rows.add(new Row(INDENT + LABEL_ID, String.valueOf(associationNodeOne.getFromGenObject().getId()), OBJECT));
			rows.add(new Row(INDENT + LABEL_MNEMONIC, associationNodeOne.getFromGenObject().getObjMetaType().name(),
					OBJECT));
			String name = associationNodeOne.getFromGenObject().findTextProperty(PrpMetaType.NAME);
			if (name.length() > 0) {
				rows.add(new Row(INDENT + LABEL_NAME, name, OBJECT));
			}
			rows.add(new Row(INDENT + LABEL_OBJECT_TYPE_CODE,
					String.valueOf(associationNodeOne.getFromGenObject().getObjMetaType().code), OBJECT));
			// association
			rows.add(new Row(LABEL_ASSOCIATION, "", null));
			rows.add(new Row(INDENT + LABEL_MNEMONIC, associationNodeOne.getAscMetaType().name(), image));
			rows.add(new Row(INDENT + LABEL_ASSOCIATION_TYPE_CODE,
					String.valueOf(associationNodeOne.getAscMetaType().code), image));
			rows.add(new Row(INDENT + LABEL_CARDINALITY,
					meta.isAssociationOnetoOne(objTypeCode, associationNodeOne.getAscMetaType()) ? "1" : "M", image));
			short inverseTypeCode = meta.getAssociationInverse(objTypeCode, associationNodeOne.getAscMetaType());
			rows.add(new Row(INDENT + LABEL_INVERSE_TYPE_MNEMONIC, meta.getAscMetaType(inverseTypeCode).name(), image));
			rows.add(new Row(INDENT + LABEL_INVERSE_TYPE_CODE, String.valueOf(inverseTypeCode), image));
			rows.add(new Row(INDENT + LABEL_OPTIONALITY,
					meta.isAssociationOptional(objTypeCode, associationNodeOne.getAscMetaType()) ? "O" : "M", image));
			rows.add(new Row(INDENT + LABEL_ORDER,
					meta.isAssociationOrdered(objTypeCode, associationNodeOne.getAscMetaType()) ? "Y" : "N", image));
			// destination
			rows.add(new Row(LABEL_DESTINATION_OBJECT, "", null));
			rows.add(new Row(INDENT + LABEL_ID, String.valueOf(associationNodeOne.getToGenObject().getId()), OBJECT));
			rows.add(new Row(INDENT + LABEL_MNEMONIC, associationNodeOne.getToGenObject().getObjMetaType().name(),
					OBJECT));
			name = associationNodeOne.getToGenObject().findTextProperty(PrpMetaType.NAME);
			if (name.length() > 0) {
				rows.add(new Row(INDENT + LABEL_NAME, name, OBJECT));
			}
			rows.add(new Row(INDENT + LABEL_OBJECT_TYPE_CODE,
					String.valueOf(associationNodeOne.getToGenObject().getObjMetaType().code), OBJECT));
			//
			tableViewer.setInput(rows);
		}
	}

	@Inject
	void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) AssociationNodeMany associationNodeMany) {
		if (associationNodeMany != null) {
			MetaHelper meta = associationNodeMany.getFromGenObject().genContainer.meta;
			ObjMetaType objTypeCode = associationNodeMany.getFromGenObject().getObjMetaType();
			Image image = meta.isAssociationForward(objTypeCode, associationNodeMany.getAscMetaType()) ? FORWARD
					: BACKWARD;
			rows.clear();
			rows.add(new Row(LABEL_SOURCE_OBJECT, "", null));
			rows.add(
					new Row(INDENT + LABEL_ID, String.valueOf(associationNodeMany.getFromGenObject().getId()), OBJECT));
			rows.add(new Row(INDENT + LABEL_MNEMONIC, associationNodeMany.getFromGenObject().getObjMetaType().name(),
					OBJECT));
			String name = associationNodeMany.getFromGenObject().findTextProperty(PrpMetaType.NAME);
			if (name.length() > 0) {
				rows.add(new Row(INDENT + LABEL_NAME, name, OBJECT));
			}
			rows.add(new Row(INDENT + LABEL_OBJECT_TYPE_CODE,
					String.valueOf(associationNodeMany.getFromGenObject().getObjMetaType().code), OBJECT));
			// association
			rows.add(new Row(LABEL_ASSOCIATION, "", null));
			rows.add(new Row(INDENT + LABEL_MNEMONIC, associationNodeMany.getAscMetaType().name(), image));
			rows.add(new Row(INDENT + LABEL_ASSOCIATION_TYPE_CODE,
					String.valueOf(associationNodeMany.getAscMetaType().code), image));
			rows.add(new Row(INDENT + LABEL_CARDINALITY,
					meta.isAssociationOnetoOne(objTypeCode, associationNodeMany.getAscMetaType()) ? "1" : "M", image));
			short inverseTypeCode = meta.getAssociationInverse(objTypeCode, associationNodeMany.getAscMetaType());
			rows.add(new Row(INDENT + LABEL_INVERSE_TYPE_MNEMONIC, String.valueOf(inverseTypeCode), image));
			rows.add(new Row(INDENT + LABEL_INVERSE_TYPE_CODE, String.valueOf(inverseTypeCode), image));
			rows.add(new Row(INDENT + LABEL_OPTIONALITY,
					meta.isAssociationOptional(objTypeCode, associationNodeMany.getAscMetaType()) ? "O" : "M", image));
			rows.add(new Row(INDENT + LABEL_ORDER,
					meta.isAssociationOrdered(objTypeCode, associationNodeMany.getAscMetaType()) ? "Y" : "N", image));
			// destination
			rows.add(new Row(LABEL_DESTINATION_OBJECT, "[*]", null));

			tableViewer.setInput(rows);
		}
	}

	private void addObjectProperies(JGenObject genObject) {
		MetaHelper meta = genObject.genContainer.meta;
		rows.add(new Row(LABEL_PROPERTIES, "", null));
		ObjMetaType objTypeCode = genObject.getObjMetaType();
		List<PrpMetaType> list = meta.getPropertyCodes(genObject.getObjMetaType());
		for (PrpMetaType prpTypeCode : list) {
			switch (meta.getPropertyFormat(objTypeCode, prpTypeCode)) {
			case "ART":
			case "TEXT":
			case "LOADNAME":
			case "NAME":
				addObjectTextProperies(genObject, prpTypeCode);
				break;
			case "CHAR":
				addObjectCharacterProperies(genObject, prpTypeCode);
				break;
			case "SINT":
			case "INT":
				addObjectNumberProperies(genObject, prpTypeCode);
				break;
			default:
				break;
			}
		}
	}

	private void addObjectNumberProperies(JGenObject genObject, PrpMetaType prpTypeCode) {
		String text = String.valueOf(genObject.findNumberProperty(prpTypeCode));
		rows.add(new Row(INDENT + prpTypeCode.name(), text, PROPERTY));
	}

	private void addObjectCharacterProperies(JGenObject genObject, PrpMetaType prpTypeCode) {
		String text = String.valueOf(genObject.findCharacterProperty(prpTypeCode));
		rows.add(new Row(INDENT + prpTypeCode.name(), text, PROPERTY));
	}

	private void addObjectTextProperies(JGenObject genObject, PrpMetaType prpTypeCode) {
		String text = genObject.findTextProperty(prpTypeCode);
		rows.add(new Row(INDENT + prpTypeCode.name(), text, PROPERTY));
	}

	private static Image getImage(String file) {
		Bundle bundle = FrameworkUtil.getBundle(WalkencyLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();
	}

}