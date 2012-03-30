dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            // dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            // url = "jdbc:hsqldb:mem:devDB"            // url = "jdbc:hsqldb:mem:devDB"
            driverClassName = "com.mysql.jdbc.Driver"
            dbCreate =  "update" // "create-drop"           // "create"
            username = "k-int"
            password = "k-int"
            // url = "jdbc:mysql://localhost/OFSDev?autoReconnect=true&amp;characterEncoding=utf8"
            url = "jdbc:mysql://localhost/OFSDef?autoReconnect=true&amp;characterEncoding=utf8"
            properties {
                validationQuery="select 1"
                testWhileIdle=true
                timeBetweenEvictionRunsMillis=60000
            }
        }
    }
    test {
        dataSource {
            //dbCreate = "update"
            //url = "jdbc:hsqldb:mem:testDb"
            driverClassName = "com.mysql.jdbc.Driver"
            dbCreate =  "update" // "create-drop"           // "create"
            username = "k-int"
            password = "k-int"
            url = "jdbc:mysql://localhost/OFSTest?autoReconnect=true&amp;characterEncoding=utf8"
            properties {
                validationQuery="select 1"
                testWhileIdle=true
                timeBetweenEvictionRunsMillis=60000
            }
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "k-int"
            password = "k-int"
            url = "jdbc:mysql://localhost/OFSProd?autoReconnect=true&amp;characterEncoding=utf8"
            properties {
                validationQuery="select 1"
                testWhileIdle=true
                timeBetweenEvictionRunsMillis=60000
            }
        }
    }
}
