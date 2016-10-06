package org.vaadin.presentation.views;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.backend.PersonService;
import org.vaadin.backend.domain.Person;
import org.vaadin.backend.domain.PersonStatus;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsFunnel;
import com.vaadin.addon.charts.model.Stacking;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;

/**
 * An example view that just make some simple analysis for the data and displays
 * it in various charts.
 */
@CDIView("analyze")
@ViewMenuItem(icon = FontAwesome.BAR_CHART_O, order = 1)
public class AnalyzeView extends MVerticalLayout implements View {

    @Inject
    PersonService service;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        removeAllComponents();

        setMargin(new MMarginInfo(false, true));
        add(new Header("Person analysis").setHeaderLevel(2));

        List<Person> personData = service.findAll();
        add(ageDistribution(personData));
        final Component funnel = createStatusFunnel(personData);
    }

    private static Panel wrapInPanel(Chart chart, String caption) {
        Panel panel = new Panel(caption, chart);
        panel.setHeight("300px");
        chart.setSizeFull();
        return panel;
    }

    private enum AgeGroup {

        Children(0, 15), Young(15, 30), MiddleAged(30, 60), Old(60, 100);

        private final int min;
        private final int max;

        AgeGroup(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public static AgeGroup getAgeGroup(Date birthDate) {
            int age = new Date().getYear() - birthDate.getYear();
            for (AgeGroup g : AgeGroup.values()) {
                if (age <= g.max && age > g.min) {
                    return g;
                }
            }
            return Old;
        }
    }

    private Chart getBasicChart(ChartType type) {
        Chart chart = new Chart(type);
        // title from panel
        chart.getConfiguration().setTitle("");
        return chart;
    }

    private Component ageDistribution(List<Person> customerData) {
        Integer[] menValues = new Integer[AgeGroup.values().length];
        Integer[] womenValues = new Integer[AgeGroup.values().length];
        for (int i = 0; i < AgeGroup.values().length; i++) {
            menValues[i] = 0;
            womenValues[i] = 0;
        }
        for (Person c : customerData) {
        }

        Chart chart = getBasicChart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();

        XAxis xAxis = new XAxis();
        String[] names = new String[AgeGroup.values().length];
        for (AgeGroup g : AgeGroup.values()) {
            names[g.ordinal()] = String.format("%s-%s", g.min,
                    g.max);
        }
        xAxis.setCategories(names);
        conf.addxAxis(xAxis);

        conf.getyAxis().setTitle("");

        Legend legend = new Legend();
        legend.setHorizontalAlign(HorizontalAlign.RIGHT);
        legend.setFloating(true);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(-5);
        legend.setY(5);
        conf.setLegend(legend);

        PlotOptionsColumn plotOptions = new PlotOptionsColumn();
        plotOptions.setStacking(Stacking.NORMAL);
        conf.setPlotOptions(plotOptions);

        conf.addSeries(new ListSeries("Men", menValues));
        conf.addSeries(new ListSeries("Women", womenValues));

        return wrapInPanel(chart, "Age distribution");

    }

    private Component createStatusFunnel(List<Person> personData) {
        int[] values = new int[PersonStatus.values().length];
        for (Person c : personData) {
            if (c.getStatus() != null) {
                values[c.getStatus().ordinal()]++;
            }
        }
        Chart chart = getBasicChart(ChartType.FUNNEL);
        DataSeries dataSeries = new DataSeries();
        dataSeries.add(new DataSeriesItem("Inactive",
                values[PersonStatus.Inactive.ordinal()]));
        dataSeries.add(new DataSeriesItem("Active",
                values[PersonStatus.Active.ordinal()]));

        Configuration conf = chart.getConfiguration();
        conf.getChart().setMarginRight(75);

        PlotOptionsFunnel options = new PlotOptionsFunnel();
        options.setNeckWidthPercentage(30);
        options.setNeckHeightPercentage(30);

        options.setWidthPercentage(70);

        dataSeries.setPlotOptions(options);
        conf.addSeries(dataSeries);

        return wrapInPanel(chart, "Sales funnel");
    }
}
