package org.samye.dzong.london.google

import com.google.gdata.client.analytics.AnalyticsService
import com.google.gdata.util.ServiceException
import com.google.gdata.data.analytics.AccountFeed
import com.google.gdata.client.analytics.DataQuery
import com.google.gdata.data.analytics.AccountEntry
import com.google.gdata.data.analytics.DataFeed
import org.joda.time.*
import java.text.SimpleDateFormat
import org.joda.time.format.*
import com.google.gdata.data.analytics.DataEntry
import org.codehaus.groovy.grails.commons.ConfigurationHolder

def as = new GoogleAnalyticsService()
as.past30DayVisits();

class GoogleAnalyticsService {
    def myService
    static final String ACCOUNTS_URL = "https://www.google.com/analytics/feeds/accounts/default";
    static final String DATA_URL = "https://www.google.com/analytics/feeds/data";
    static final String TABLE_ID = "ga:XXXX";
    static final String USERNAME = "acct"
    static final String PASSWORD = "pass"

    GoogleAnalyticsService() {
        try {
            myService = new AnalyticsService("ksdl-webapp-analytics");
            if (PASSWORD != "xXxXxXxXxXx") {
                myService.setUserCredentials(USERNAME, PASSWORD);
            }
        } catch(error) {
            log.error "Failed to log on to Google Analytics for Statistics.", error
        }
    }

    def past30DayVisits() {
        def results = []
        try {
            URL feedUrl = new URL(ACCOUNTS_URL);
            AccountFeed feed = myService.getFeed(feedUrl, AccountFeed.class);

            // Print the results of a basic request
            DataQuery basicQuery = getPastMonthNewVisits(TABLE_ID);
            DataFeed dataFeed = myService.getFeed(basicQuery, DataFeed.class);
            for (DataEntry entry : dataFeed.getEntries()) {
                def value = [date: Date.parse("yyyyMMdd",entry.stringValueOf("ga:date")), visits: [newVisitors:entry.stringValueOf("ga:visitors").toInteger()]]
                results << value
            }

            basicQuery = getPastMonthReturningVisits(TABLE_ID);
            dataFeed = myService.getFeed(basicQuery, DataFeed.class);
            for (DataEntry entry : dataFeed.getEntries()) {
                def values = results.find{it.date == Date.parse("yyyyMMdd",entry.stringValueOf("ga:date"))}
                def v1 = values.get('visits')
                v1.put('returningVisitors', entry.stringValueOf("ga:visitors").toInteger())
            }

            basicQuery = getPastMonthAllVisits(TABLE_ID);
            dataFeed = myService.getFeed(basicQuery, DataFeed.class);
            for (DataEntry entry : dataFeed.getEntries()) {
                def values = results.find{it.date == Date.parse("yyyyMMdd",entry.stringValueOf("ga:date"))}
                def v1 = values.get('visits')
                v1.put('totalVisitors', entry.stringValueOf("ga:visitors").toInteger())
            }
        } catch (ServiceException se) {
            log.error "", se
        } catch (IOException ioe) {
            log.error "", ioe
        }
        results
    }

    def top10Pages() {
        def topTen = []
        try {
            URL feedUrl = new URL(ACCOUNTS_URL);
            AccountFeed feed = myService.getFeed(feedUrl, AccountFeed.class);

            // Print the results of a basic request
            DataQuery basicQuery = getTopPagesQuery(TABLE_ID, 10);
            DataFeed dataFeed = myService.getFeed(basicQuery, DataFeed.class);
            for (DataEntry entry : dataFeed.getEntries()) {
                def path = entry.stringValueOf("ga:pagePath")
                path = path.startsWith("/app") ? path.substring(4) : path
                def title = entry.stringValueOf("ga:pageTitle")
                title = title.contains("Kagyu Samye Dzong London: ") ? title.substring(26) : title
                topTen << [title: title, path: path, visits: entry.stringValueOf("ga:pageviews")]
            }
        } catch (ServiceException se) {
            log.error "", se
        } catch (IOException ioe) {
            log.error "", ioe
        }
        topTen
    }

    def top5Pages() {
        def topTen = []
        try {
            URL feedUrl = new URL(ACCOUNTS_URL);
            AccountFeed feed = myService.getFeed(feedUrl, AccountFeed.class);

            // Print the results of a basic request
            DataQuery basicQuery = getTopPagesQuery(TABLE_ID, 5);
            DataFeed dataFeed = myService.getFeed(basicQuery, DataFeed.class);
            for (DataEntry entry : dataFeed.getEntries()) {
                def path = entry.stringValueOf("ga:pagePath")
                path = path.startsWith("/app") ? path.substring(4) : path
                topTen << [title: entry.stringValueOf("ga:pageTitle"), path: path, visits: entry.stringValueOf("ga:pageviews")]
            }
        } catch (ServiceException se) {
            log.error "", se
        } catch (IOException ioe) {
            log.error "", ioe
        }
        topTen
    }

    DataQuery getTopPagesQuery(tableId, numberOfResults) throws MalformedURLException {
        def now = new java.util.Date()
        now.clearTime()
        def dt = new DateTime(now.getTime())
        DateTime startOfMonth = dt.dayOfMonth().withMinimumValue();
        DateTime endOfMonth = dt.dayOfMonth().withMaximumValue();

        // Set up the request (we could alternately construct a URL manually with all query parameters set)
        DataQuery query = new DataQuery(new URL(DATA_URL));
        query.setIds(tableId);
        query.setStartDate(startOfMonth.toDate().format('yyyy-MM-dd'));
        query.setEndDate(endOfMonth.toDate().format('yyyy-MM-dd'));
        query.setDimensions("ga:pageTitle,ga:pagePath");
        query.setMetrics("ga:pageviews");
        query.setSort("-ga:pageviews");
        query.setMaxResults(numberOfResults);

        return query;
    }

    DataQuery getPastMonthNewVisits(tableId) throws MalformedURLException {
        def now = new java.util.Date()-1
        now.clearTime()
        DateTime end = new DateTime(now.getTime())
        DateTime start = end.minusMonths(1);

        // Set up the request (we could alternately construct a URL manually with all query parameters set)
        DataQuery query = new DataQuery(new URL(DATA_URL));
        query.setIds(tableId);
        query.setStartDate(start.toDate().format('yyyy-MM-dd'));
        query.setEndDate(end.toDate().format('yyyy-MM-dd'));
        query.setDimensions("ga:date");
        query.setMetrics("ga:visitors");
        query.setSegment("gaid::-2");
        query.setMaxResults(500);

        return query;
    }

    DataQuery getPastMonthReturningVisits(tableId) throws MalformedURLException {
        def now = new java.util.Date()-1
        now.clearTime()
        DateTime end = new DateTime(now.getTime())
        DateTime start = end.minusMonths(1);

        // Set up the request (we could alternately construct a URL manually with all query parameters set)
        DataQuery query = new DataQuery(new URL(DATA_URL));
        query.setIds(tableId);
        query.setStartDate(start.toDate().format('yyyy-MM-dd'));
        query.setEndDate(end.toDate().format('yyyy-MM-dd'));
        query.setDimensions("ga:date");
        query.setMetrics("ga:visitors");
        query.setSegment("gaid::-3");
        query.setMaxResults(500);

        return query;
    }
    DataQuery getPastMonthAllVisits(tableId) throws MalformedURLException {
        def now = new java.util.Date()-1
        now.clearTime()
        DateTime end = new DateTime(now.getTime())
        DateTime start = end.minusMonths(1);

        // Set up the request (we could alternately construct a URL manually with all query parameters set)
        DataQuery query = new DataQuery(new URL(DATA_URL));
        query.setIds(tableId);
        query.setStartDate(start.toDate().format('yyyy-MM-dd'));
        query.setEndDate(end.toDate().format('yyyy-MM-dd'));
        query.setDimensions("ga:date");
        query.setMetrics("ga:visitors");
        query.setSegment("gaid::-1");
        query.setMaxResults(500);

        return query;
    }
}
