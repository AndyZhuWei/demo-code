背景

字节和字符的区分

为什么是varchar(255)而不是(256)

varchar字段最大值是多少

latin1字符集编码下

utf8字符集编码下

utf8mb4字符集编码下

总结


背景

你是否知道MySQL中的varchar字段类型最大能够存储多少数据？它的最大长度是多少？为什么有时候定义一个varchar(10)的字段可以存储10个汉字，
但是不能存储超过10个长度的字母呢？为什么有时候我定义的varchar(10)的字段又存储不了10个汉字呢？为什么很多地方在定义varchar类型的字段的时候，
经常使用varchar(255)？而不是使用varchar(256)呢？

读完下面的文章，你将会解开这些谜团。

字节和字符的区分

首先，我们要知道字节和字符的含义是什么，什么是字节？什么是字符？

字节(byte)：这个应该很好理解，字节是计算机中的存储单位：1GB=1024MB，1MB=1024KB，1KB=1024Byte，1Byte=8Bit。其中的Byte就是字节。

字符(character)：可以理解为一个数字、字母、汉字、标点符号，比如：数字1是一个字符，数字9也是一个字符，但是数字10就是两个字符；字母a就是一个字符，
字母B也是一个字符，字母aB就是两个字符；汉字你也是一个字符，汉字我也是一个字符，汉字你我就是两个字符；标点符号?是一个字符，标点符号!也是一个字符，
当然这里的标点符号有全角和半角之分，但是不管全角还是半角，它们都是属于不同的字符，也就是说半角的?和全角的？是两个不同的字符。

当我们了解了字节和字符的区别之后，接下来，我们看下这个：varchar(10)，这里面的10表示的是什么含义？在MySQL的不同版本中，这个10的含义是不同的，不能一概而论。

在MySQL4.0之前的版本中，这个10的含义是10个字节byte，也就是说这个字段最大能存放10个byte字节长度的内容。

在MySQL5.0以后的版本中，这个10的含义就是10个字符character，也就是说这个字段最大能存放10个character字符长度的内容。



那么在判断这个varchar(10)的字段到底能存多少数据内容的时候，又需要一个字符集的介入，不同的字符集下，同一个字符存储到表中的时候，
它所占用的空间大小是不同的。一个字符character存储在表中，它到底占用多少个字节byte呢？这个需要根据不同的字符集来分别计算。

常用的几种字符集下，字符character和字节byte的换算关系如下：

GBK编码下：1个字符占用2个byte字节，此时一个字母a和一个汉字你都是占用2个byte字节。

UTF8编码下：1个字符占用3个byte字节，此时一个字母a和一个汉字你都是占用3个byte字节。

UTF8MB4编码下：1个字符占用4个byte字节，此时一个字母a和一个汉字你都是占用4个byte字节。

MySQL默认的latin1拉丁文编码下：1个字符占用1个byte字节。此时一个字母a占用1个byte字节，该字符集编码下，不支持插入中文字符，所以不用考虑中文字符占用多少个字节了。

这里简单的提一下utf8和utf8mb4的区别：

MySQL中的utf8在很早的时候就存在了，但是此时的utf8和大家概念中的utf8不太一样，它在内部实现的时候，只能识别大家经常用到的一些字符。
但是对于一些特殊的表情符号，就是大家微信中各种笑脸、红心、鲜花等表情符号，在其他软件中utf8中是实现了的，但是在MySQL的utf8中还没有实现。
后续，为了支持这些表情符号，MySQL重构了原先的utf8，让其支持这些符号，然后这个字符集命名为utf8mb4。你可以认为utf8是utf8mb4的子集。
utf8支持的字符，在utf8mb4中一定支持，但是utf8mb4支持的字符，在utf8中并不一定支持。

为什么是varchar(255)而不是(256)

为什么推荐使用varchar255，而不推荐使用varchar256呢？在很多的数据库客户端工具中，大多数的客户端在创建一个varchar类型的字段的时候，
如果不指定字段的长度，默认都是使用255，为什么不是256呢？按照我们的平时的逻辑：16、32、64、128、256、512...这样不是很好吗？为什么这里是255，而不是256呢？

因为varchar类型的字段长度在超过255后，比如我们使用varchar(256)，varchar(256)的字段在存储到磁盘的时候，
就会比varchar(255)的字段多占用1个byte的存储空间。而多出来的这一个byte的空间用来存储该字段的长度用了。

我们在定义一个字段varchar(1)的时候，数据库在底层要知道这个字段的最大长度是多少，这里我们定义为1，那么他就知道这个字段的长度为1，要记录好这个1；
如果我们定义为255，它在底层就要记录上这个字段的长度为255。

但是数据在底层存储的时候，它不是直接把1或255这样的整数直接以十进制的方式存储的，它是把1和255这样的整数转换为2进制的方式来存储的，
全部都是0或1的方式来存储。而我们知道tinyint类型的数据在MySQL底层存储的时候会占用1个byte的存储空间。
具体可以参考下面的表格。每一种int类型的取值范围是不同的，如下表格所示：


通过上面的表格我们可以看出，当我们定义的字段长度超过255之后，它在底层存储的时候就会占用2个byte，因为一个8位的tinyint，
可以表示的无符号数值的范围是，0-255，当超过255之后，就要升级为smallint占用2个byte了。255刚好是smallint的最大存储值。

当选择的字符集为latin1，一个字符占用一个byte

varchar(255)存储一个字符a的时候，使用1个byte表示字段的长度，1个byte用来存储字符a，此时一共使用2个bytes物理存储空间。

varchar(256)存储一个字符a的时候，使用2 bytes表示字段的长度，1个byte用来存储字符a，此时一共需要3 bytes物理存储空间。

当实际长度大于255的时候，varchar变长字段长度列表需要用两个字节存储，也就意味着每一行数据都会增加1个字节，所以在我们的数据长度不可能超过255的情况下，
我们尽量不要创建超过255长度的varchar类型的字段。

了解了为什么varchar(256)在存储同样的数据内容的时候比varchar(255)多占用1个byte的空间之后，我们继续往下看。

varchar字段最大值是多少

MySQL官网中没有直接给出这个值是多少，但是它给出了表中的一行数据的最大长度为65535个byte。假如我们把一个表只设置1个列，
那么这个列的最大长度应该就应该等于MySQL中所允许的单行最大的长度值，也就是65535。

我们接下来看一个例子。我们创建一个表这个表只有一个列：remark，字段类型为varchar，长度我们设置为65535。看下效果是怎么样的。

我们下面分别使用三种字符集编码的方式来创建三个表。这三个字符集编码分别为：latin1、utf8、utf8mb4。

latin1字符集编码下

在该字符集编码下面，一个字符占用一个字节，我们的实验过程如下所示，

mysql> create table test_latin1(remark varchar(65535))charset=latin1; 
ERROR 1118 (42000): Row size too large. The maximum row size for the used table type, not counting BLOBs, is 65535. 
This includes storage overhead, check the manual. You have to change some columns to TEXT or BLOBs 
mysql> create table test_latin1(remark varchar(65534))charset=latin1; 
ERROR 1118 (42000): Row size too large. The maximum row size for the used table type, not counting BLOBs, is 65535.
 This includes storage overhead, check the manual. You have to change some columns to TEXT or BLOBs 
mysql> create table test_latin1(remark varchar(65533))charset=latin1;
 ERROR 1118 (42000): Row size too large. The maximum row size for the used table type, not counting BLOBs, is 65535. 
 This includes storage overhead, check the manual. You have to change some columns to TEXT or BLOBs
  mysql> create table test_latin1(remark varchar(65532))charset=latin1;
   Query OK, 0 rows affected (0.02 sec) 
   mysql> show create table test_latin1\G
    *************************** 1. row *************************** 
    Table: test_latin1 Create Table: CREATE TABLE `test_latin1` ( `remark` varchar(65532) DEFAULT NULL )
     ENGINE=InnoDB DEFAULT CHARSET=latin1 1 row in set (0.01 sec) 
     mysql> select 65535 - 65532; +---------------+ | 65535 - 65532 | +---------------+ | 3 | +---------------+ 1 row in set (0.00 sec)
     mysql> 

经过测试后发现，65535并不是最大限制，最大的限制是65532，两者之间差值为3，我们已经知道字符串的长度超过255后，需要占用2个字节byte，
这里要在65535基础上减去2，然后还有``1个字节用在哪里了呢？其实这一个字节用于存储字段是否可以为null的标识。
当我们设置表中的字段不能为空的时候，就可以省去这一个字节的空间。我们可以参考下面的这个实验，
它和上面的实验的唯一区别就是在字段中增加了not null`非空约束。

mysql> create table test_latin1_col_not_null(remark varchar(65535) not null)charset=latin1;
 ERROR 1118 (42000): Row size too large. The maximum row size for the used table type, not counting BLOBs, is 65535.
  This includes storage overhead, check the manual. You have to change some columns to TEXT or BLOBs 
  mysql> create table test_latin1_col_not_null(remark varchar(65534) not null)charset=latin1;
   ERROR 1118 (42000): Row size too large. The maximum row size for the used table type, not counting BLOBs, is 65535.
    This includes storage overhead, check the manual. You have to change some columns to TEXT or BLOBs 
    mysql> create table test_latin1_col_not_null(remark varchar(65533) not null)charset=latin1; 
    Query OK, 0 rows affected (0.02 sec) 
    mysql> show create table test_latin1_col_not_null\G 
    *************************** 1. row *************************** 
    Table: test_latin1_col_not_null 
    Create Table: CREATE TABLE `test_latin1` ( `remark` varchar(65533) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=latin1
     1 row in set (0.00 sec) 
     mysql> select 65535 - 65533; +---------------+ | 65535 - 65533 | +---------------+ | 2 | +---------------+ 1 row in set (0.01 sec)


utf8字符集编码下

经过上面在latin1字符集实验之后，我们知道varchar类型的字段，需要1个字节用于存储字符是否为null的标识，另外如果字段长度超过255之后，需要2个字节，
存储字段的长度，如果字段小于255，则用1个字节存储字段长度。

那么在utf8字符集编码下，一个字符占用3个字节。我们的实验如下：

mysql> create table test_utf8(remark varchar(65535))charset=utf8; 
ERROR 1074 (42000): Column length too big for column 'remark' (max = 21845); 
use BLOB or TEXT instead 
/*上面的错误提示，utf8编码下，最大的长度为21845。但是其实并不是这个长度，下面的这个创建表的SQL语句可以证明。
即时我们设置字段的长度为提示的这个值，仍然不能创建成功。 */
 mysql> create table test_utf8(remark varchar(21845))charset=utf8;
  ERROR 1118 (42000): Row size too large. The maximum row size for the used table type, not counting BLOBs, is 65535.
   This includes storage overhead, check the manual. You have to change some columns to TEXT or BLOBs 
   /*上面的错误提示，和第一次创建失败的提示有些不同，这里提示是超过最大行的大小限制了，和前面我们使用litan1字符编码的时候，
   设置字段长度为65535的时候提示信息是相同的。所以我们减少一个字符试试看能否成功*/ 
   mysql> create table test_utf8(remark varchar(21844))charset=utf8;
    Query OK, 0 rows affected (0.02 sec) 
    mysql> show create table test_utf8\G 
    *************************** 1. row *************************** 
    Table: test_utf8 Create Table: CREATE TABLE `test_utf8` ( `remark` varchar(21844) DEFAULT NULL ) 
    ENGINE=InnoDB DEFAULT CHARSET=utf8 1 row in set (0.01 sec) /*表创建成功了*/ 
    mysql> select 65535 / 
    3;/*为什么不是这个值呢？而是21845-1=21844呢？
    */ +------------+ | 65535 / 3 | +------------+ | 21845.0000 | +------------+ 1 row in set (0.01 sec) 
    mysql> select (65535 - 2 - 1) / 
    3;/*这里要在最大长度的基础上减去2个byte，用于存储字段的长度，因为字段的长度大于255这个值了，
    另外，还需要1个byte用于存储字段是可以为null的标识。
    */ +---------------------+ | (65535 - 2 - 1) / 3 | +---------------------+ | 21844.0000 | +---------------------+ 
    1 row in set (0.01 sec) mysql> 

在utf8编码下面，也需要在行所支持的最大长度65535之上，减去2两个byte用于存储字段长度，再减去1一个byte字段长度用于存储是否可以为null的标识。
得到的结果再去除以3(一个字符占用3个byte)，才是最后utf8编码下面varchar字段类型可以定义的最大长度。

此时，如果我们把表的字段定义为不能为空的话，是否可以加长varchar字段的长度呢？看下结果：

mysql> create table test_utf8_col_not_null(remark varchar(65535) not null)charset=utf8; 
ERROR 1074 (42000): Column length too big for column 'remark' (max = 21845); use BLOB or TEXT instead 
mysql> create table test_utf8_col_not_null(remark varchar(21845) not null)charset=utf8;
 ERROR 1118 (42000): Row size too large. The maximum row size for the used table type, not counting BLOBs, is 65535.
  This includes storage overhead, check the manual. You have to change some columns to TEXT or BLOBs
   mysql> create table test_utf8_col_not_null(remark varchar(21844) not null)charset=utf8; 
   Query OK, 0 rows affected (0.03 sec) mysql> show create table test_utf8_col_not_null\G 
   *************************** 1. row *************************** Table: test_utf8_col_not_null Create Table: 
   CREATE TABLE `test_utf8_col_not_null` ( `remark` varchar(21844) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8
    1 row in set (0.00 sec) mysql> select (65535 - 2) / 3;/*此时即便是设置了not null非空约束，可以省去1个byte字节的大小，
    但是结算的结果中，会对结果向下取整，舍弃小数点后面的值。得到的结果和多减去1的结果是一致的。
    */ +-----------------+ | (65535 - 2) / 3 | +-----------------+ | 21844.3333 | +-----------------+ 
    1 row in set (0.00 sec) mysql> 

我们发现即便使我们把字段的类型设置为not null，utf8字符集编码下面，varchar类型的最大长度也不能有所增加，这是因为得到的结果中，会向下取整，
即对21844.3333向下取整，得到21844，和多减去1再除以3之后的结果一致。所以这里即便是把varchar字段设置为非空，也不能增大varchar字段定义的长度的阈值。

utf8mb4字符集编码下

在utf8mb4字符集编码下，一个字符占用4个字节。我们的实验过程如下：

mysql> create table test_utf8mb4(remark varchar(65535))charset=utf8mb4;
 ERROR 1074 (42000): Column length too big for column 'remark' (max = 16383);
  use BLOB or TEXT instead mysql> create table test_utf8mb4(remark varchar(16383))charset=utf8mb4; 
  Query OK, 0 rows affected (0.03 sec) mysql> show create table test_utf8mb4\G 
  *************************** 1. row ***************************
   Table: test_utf8mb4 Create Table: 
   CREATE TABLE `test_utf8mb4` ( `remark` varchar(16383) DEFAULT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 
   1 row in set (0.01 sec)
    mysql> select (65535 - 2 - 1) / 4; +---------------------+ | (65535 - 2 - 1) / 4 | +---------------------+ 
    | 16383.0000 | +---------------------+ 1 row in set (0.01 sec) mysql> select (65535 - 2) / 
    4;/* 从这个结果中可以看出，即便还是设置字段的非空约束，也不能增加varchar字段定义时候的最大长度阈值。
    */ +-----------------+ | (65535 - 2) / 4 | +-----------------+ | 16383.2500 | +-----------------+ 
    1 row in set (0.01 sec) mysql> 

通过上面的实验，我们可以知道在utf8mb4字符集编码下，varcahr字段的最大长度为16383，即便是我们设置字段值为非空约束，也不能增加这个阈值。
因为(65535 - 2) / 4向下取整的值和(65535 - 2 - 1) / 4的值是一样的。

如果是GBK字符集编码，也是同样的逻辑，floor((65535 -2 -1) /2) = 32766，所以32766就是GBK编码下，varchar类型的子弹最大的长度。

总结

在MySQL4.0版本之前，varchar类型字段定义的时候指定的长度是以字节byte为单位的。

在5.0之后的版本中，都是使用字符为单位。我觉得这个还是很友好的，起码我看到这个长度我就知道这个字段最大能存储多少个字符了。
而不用费劲心思再计算一下每一个字符集下面每一个字符占用多少个字节，然后再根据字节的长度推测出这个字段可以存储多少个字符。

在MySQL5.7版本中，varchar字段最大的长度是多少？不同的字符集下，一个字符所占用的磁盘空间是不同的，所以：

litan1字符集下，varchar最大可以可以存储65535-2-1=65532个字符。如果是非空的varchar字段，可以存储65535-2=65533个字符。

utf8字符集下，varchar最大可以可以存储floor((65535-2-1)/3)=21844个字符。如果是非空的varchar字段，可以存储floor((65535-2)/3)=21844，向下取整后，也是21844个字符。

utf8mb4字符集下，varchar最大可以可以存储floor((65535-2-1)/4)=16383个字符。如果是非空的varchar字段，可以存储floor((65535-2)/4)=16383，向下取整后，也是16383个字符。

我们平时的开发当中，定义表结构的时候，如果是varchar类型的字段，一般情况下255应该就是够用了。如果不够用，可以适当增加长度。如果还是不够用，就使用text类型。

