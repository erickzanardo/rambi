importClass(java.util.Calendar);
importClass(java.util.GregorianCalendar);
importClass(java.util.List);

var utils = {
        // JSON data type to JAVA
        jsonToJavaType : function(v) {
            if (typeof (v) == 'number') {
                if (v % 1 === 0) {
                    return new Long(v);
                } else {
                    return new Double(v);
                }
            } else if (v instanceof Date) {
                var calendar = GregorianCalendar.getInstance();
                calendar.set(Calendar.DATE, v.getDate());
                calendar.set(Calendar.MONTH, v.getMonth());
                calendar.set(Calendar.YEAR, v.getFullYear());

                calendar.set(Calendar.HOUR, v.getHours());
                calendar.set(Calendar.MINUTE, v.getMinutes());
                calendar.set(Calendar.SECOND, v.getSeconds());
                calendar.set(Calendar.MILLISECOND, v.getMilliseconds());

                return calendar.getTime();
            } else if (typeof (v) == 'boolean') {
                return new java.lang.Boolean(v);
            } else if (Array.isArray(v)) {
                var list = new ArrayList();
                for (var i = 0; i < v.length; i++) {
                    list.add(this.jsonToJavaType(v[i]));
                }
                return list;
            } else {
                return v;
            }
        },

        // JAVA data type to JSON 
        javaToJsonType : function(value) {
            if (value instanceof String) {
                return '' + value;
            } else if (value instanceof java.lang.Number) {
                return new Number(value);
            } else if (value instanceof java.lang.Boolean) {
                return value ? true : false;
            } else if (value instanceof java.util.Date) {
                return new Date(value.getTime());
            } else if (value instanceof List) {
                var list = [];
                for (var i = 0; i < value.size(); i++) {
                    list.push(this.javaToJsonType(value.get(i)));
                }
                return list;
            }
        }
}