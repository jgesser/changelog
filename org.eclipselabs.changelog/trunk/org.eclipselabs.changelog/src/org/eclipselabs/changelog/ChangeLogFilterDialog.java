/*
 * Copyright (c) John C. Landers All rights reserved. This program and the accompanying materials are made available
 * under the terms of the Common Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * *****************************************************************************
 */
package org.eclipselabs.changelog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author jcl To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
final class ChangeLogFilterDialog extends Dialog {

    private ChangeLogFilter filter;

    //widgets
    private Button orRadio;

    private Button andRadio;

    private Combo fromDayCombo;

    private Combo toDayCombo;

    private Combo fromMonthCombo;

    private Combo toMonthCombo;

    private Combo fromYearCombo;

    private Combo toYearCombo;

    private Text author;

    private Text comment;

    public ChangeLogFilterDialog(ChangeLogView view) {
        super(view.getViewSite().getShell());
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("ChangeLog Filter Dialog");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite topLevel = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        topLevel.setLayout(layout);
        //"and" and "or" search radio buttons
        Label label = new Label(topLevel, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        label.setLayoutData(data);
        label.setText("Matching");
        andRadio = new Button(topLevel, SWT.RADIO);
        andRadio.setText("Matching All");
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        andRadio.setLayoutData(data);
        andRadio.setSelection(true);
        orRadio = new Button(topLevel, SWT.RADIO);
        orRadio.setText("Matching Any");
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        orRadio.setLayoutData(data);
        //author
        label = new Label(topLevel, SWT.NONE);
        label.setText("Author");
        author = new Text(topLevel, SWT.BORDER);
        author.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //comment
        label = new Label(topLevel, SWT.NONE);
        label.setText("Comment");
        comment = new Text(topLevel, SWT.BORDER);
        comment.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //"from" date
        label = new Label(topLevel, SWT.NONE);
        label.setText("FromDate");
        Composite fdComposite = new Composite(topLevel, SWT.NONE);
        GridLayout fdLayout = new GridLayout();
        fdLayout.numColumns = 3;
        fdComposite.setLayout(fdLayout);
        fromMonthCombo = new Combo(fdComposite, SWT.READ_ONLY);
        fromDayCombo = new Combo(fdComposite, SWT.READ_ONLY);
        fromYearCombo = new Combo(fdComposite, SWT.NONE);
        fromYearCombo.setTextLimit(4);
        //"to" date
        label = new Label(topLevel, SWT.NONE);
        label.setText("ToDate");
        Composite tdComposite = new Composite(topLevel, SWT.NONE);
        GridLayout tdLayout = new GridLayout();
        tdLayout.numColumns = 3;
        tdComposite.setLayout(tdLayout);
        toMonthCombo = new Combo(tdComposite, SWT.READ_ONLY);
        toDayCombo = new Combo(tdComposite, SWT.READ_ONLY);
        toYearCombo = new Combo(tdComposite, SWT.NONE);
        toYearCombo.setTextLimit(4);
        //set day, month and year combos with numbers
        //years allows a selection from the past 5 years
        //or any year written in
        String days[] = new String[32];
        days[0] = "---"; //$NON-NLS-1$
        for (int i = 1; i < 32; i++) {
            days[i] = String.valueOf(i);
        }
        String months[] = new String[13];
        months[0] = "---"; //$NON-NLS-1$
        SimpleDateFormat format = new SimpleDateFormat("MMMM"); //$NON-NLS-1$
        Calendar calendar = Calendar.getInstance();
        for (int i = 1; i < 13; i++) {
            calendar.set(Calendar.MONTH, i - 1);
            months[i] = format.format(calendar.getTime());
        }
        String years[] = new String[5];
        Calendar calender = Calendar.getInstance();
        for (int i = 0; i < 5; i++) {
            years[i] = String.valueOf(calender.get(1) - i);
        }
        fromDayCombo.setItems(days);
        fromDayCombo.select(0);
        toDayCombo.setItems(days);
        toDayCombo.select(0);
        fromMonthCombo.setItems(months);
        fromMonthCombo.select(0);
        toMonthCombo.setItems(months);
        toMonthCombo.select(0);
        fromYearCombo.setItems(years);
        toYearCombo.setItems(years);
        fromYearCombo.select(0);
        toYearCombo.select(0);
        initializeValues();
        Dialog.applyDialogFont(parent);
        return topLevel;
    }

    void initializeValues() {
        if (filter == null) {
            return;
        }
        if (filter.getAuthor() != null) {
            author.setText(filter.getAuthor());
        }
        if (filter.getComment() != null) {
            comment.setText(filter.getComment());
        }
        orRadio.setSelection(filter.isOr());
        andRadio.setSelection(!filter.isOr());
        Calendar calendar = Calendar.getInstance();
        if (filter.getFromDate() != null) {
            calendar.setTime(filter.getFromDate());
            fromDayCombo.select(calendar.get(Calendar.DATE));
            fromMonthCombo.select(calendar.get(Calendar.MONTH) + 1);
            String yearValue = String.valueOf(calendar.get(Calendar.YEAR));
            int index = fromYearCombo.indexOf(yearValue);
            if (index == -1) {
                fromYearCombo.add(yearValue);
                index = fromYearCombo.indexOf(yearValue);
            }
            fromYearCombo.select(index);
        }
        if (filter.getToDate() != null) {
            calendar.setTime(filter.getToDate());
            toDayCombo.select(calendar.get(Calendar.DATE));
            toMonthCombo.select(calendar.get(Calendar.MONTH) + 1);
            String yearValue = String.valueOf(calendar.get(Calendar.YEAR));
            int index = toYearCombo.indexOf(yearValue);
            if (index == -1) {
                toYearCombo.add(yearValue);
                index = toYearCombo.indexOf(yearValue);
            }
            toYearCombo.select(index);
        }
    }

    /**
     * A button has been pressed. Process the dialog contents.
     */
    @Override
    protected void buttonPressed(int buttonId) {
        if (IDialogConstants.CANCEL_ID == buttonId) {
            super.buttonPressed(buttonId);
            return;
        }
        Date fromDate = null;
        if ((fromMonthCombo.getSelectionIndex() > 0) && (fromDayCombo.getSelectionIndex() > 0) && (fromYearCombo.getText().length() > 0)) {
            //set the calendar with the user input
            //set the hours, minutes and seconds to 00
            //so as to cover the whole day
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(String.valueOf(fromYearCombo.getText())), fromMonthCombo.getSelectionIndex() - 1, Integer.parseInt(String.valueOf(fromDayCombo.getText())), 00, 00, 00);
            fromDate = calendar.getTime();
        }
        Date toDate = null;
        if ((toMonthCombo.getSelectionIndex() > 0) && (toDayCombo.getSelectionIndex() > 0) && (toYearCombo.getText().length() > 0)) {
            //set the calendar with the user input
            //set the hours, minutes and seconds to 23, 59, 59
            //so as to cover the whole day
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(String.valueOf(toYearCombo.getText())), toMonthCombo.getSelectionIndex() - 1, Integer.parseInt(String.valueOf(toDayCombo.getText())), 23, 59, 59);
            toDate = calendar.getTime();
        }
        //create the filter
        filter = new ChangeLogFilter(author.getText(), comment.getText(), fromDate, toDate, orRadio.getSelection());
        super.buttonPressed(buttonId);
    }

    /**
     * Returns the filter that was created from the provided user input.
     */
    public ChangeLogFilter getFilter() {
        return filter;
    }

    /**
     * Set the intial value of the dialog to the given filter.
     */
    public void setFilter(ChangeLogFilter filter) {
        this.filter = filter;
    }
}
