package com.smartgwt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.PageKeyHandler;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.viewer.DetailViewer;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ChangeManagementEntryPoint implements EntryPoint {

    private ListGrid boundList;
    private DynamicForm boundForm;
    private IButton saveBtn;
    private DetailViewer boundViewer;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {


        KeyIdentifier debugKey = new KeyIdentifier();
        debugKey.setCtrlKey(true);
        debugKey.setKeyName("D");

        Page.registerKey(debugKey, new PageKeyHandler() {
            public void execute(String keyName) {
                SC.showConsole();
            }
        });

        DataSource dataSource = DataSource.get("changeManagementDMI");

        VStack vStack = new VStack();
        vStack.setLeft(175);
        vStack.setTop(75);
        vStack.setWidth("70%");
        vStack.setMembersMargin(20);

        Label label = new Label();
        label.setContents("<ul>" +
                "<li>Change Management</li>" +
                "</ul>");
        vStack.addMember(label);

        boundList = new ListGrid();
        boundList.setHeight(200);
        boundList.setCanEdit(true);
        boundList.setDataSource(dataSource);

        boundList.addRecordClickHandler(new RecordClickHandler() {
            public void onRecordClick(RecordClickEvent event) {
                Record record = event.getRecord();
                boundForm.editRecord(record);
                saveBtn.enable();
                boundViewer.viewSelectedData(boundList);
            }
        });
        vStack.addMember(boundList);

        boundForm = new DynamicForm();
        boundForm.setDataSource(dataSource);
        boundForm.setNumCols(6);
        boundForm.setAutoFocus(false);
        vStack.addMember(boundForm);

        ToolStrip toolbar = new ToolStrip();
        toolbar.setMembersMargin(10);
        toolbar.setHeight(22);

        saveBtn = new IButton("save");
        saveBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundForm.saveData();
                if (!boundForm.hasErrors()) {
                    boundForm.clearValues();
                    saveBtn.disable();
                }
            }
        });
        toolbar.addMember(saveBtn);

        IButton clearBtn = new IButton("clear");
        clearBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundForm.clearValues();
                saveBtn.disable();
            }
        });
        toolbar.addMember(clearBtn);

        IButton filterBtn = new IButton("filter");
        filterBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundList.filterData(boundForm.getValuesAsCriteria());
                saveBtn.disable();
            }
        });
        toolbar.addMember(filterBtn);

        IButton fetchBtn = new IButton("fetch");
        fetchBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundList.fetchData(boundForm.getValuesAsCriteria());
                saveBtn.disable();
            }
        });
        toolbar.addMember(fetchBtn);

        IButton deleteBtn = new IButton("Delete");
        deleteBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boundList.removeSelectedData();
                boundList.deselectAllRecords();
            }
        });
        toolbar.addMember(deleteBtn);

        vStack.addMember(toolbar);

        boundViewer = new DetailViewer();
        boundViewer.setDataSource(dataSource);
        vStack.addMember(boundViewer);

        vStack.draw();

        boundList.filterData(null);
    }


}
