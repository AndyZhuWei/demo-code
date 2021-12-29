详细的执行计划参见13
这里补充一些信息

explian extended select * from table;  会多出一些列filtered

apache calcite:sql语法解析

在执行计划列extra中可以看到using index表示使用了覆盖索引
