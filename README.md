# XTRCT SPRING BATCH
Sample project to read the data from database (SQL server) and extract them to CSV file with help of Spring Batch project.

## Configuration Note
The Spring boot application uses Spring Batch library to use chunk oriented programming (i.e. reader, processor, writer).
The application read the records using pagination reader from SQL server database and multiple threads are initiated to extract those records to CSV files.
The following parameters have been made configurable and same can be used to tweak the performance of application. 

`records.per.read` - _Page size i.e. Number of records to be read in one page._

`records.per.writer` _Maximum records to write per thread. Once these number of records are processed (i.e. unmarshalled & ready to write), then a thread will be initialized to write them in CSV files._

`thread.pool.size` _Number of threads created by application to extract the records in CSV files._ 

## Build Notes
The xtrct-spring-batch is a Spring Boot module and the jar will be : `'xtrct-spring-batch-1.x.y.jar'`
The dist.xml build process will rename the jar as` 'xtrct-spring-batch.jar'.
The Extract jar can be run using the command:

`java -ms32m -mx4g -Xloggc:<PROJECT XTRCT DIR PATH>\logs\US_EN_2.2-gc_log.txt -Dlog4j.configuration=file:<PROJECT XTRCT DIR PATH>\log4j.properties -Dproperties.path=<PROJECT XTRCT DIR PATH>\jdbc.properties -cp <PROJECT XTRCT DIR PATH>\lib\* -jar <PROJECT XTRCT DIR PATH>\lib\xtrct-spring-batch.jar -responseDir <PROJECT XTRCT DIR PATH>\Data\ -locale US_EN -timeout 1000 1>.\Data\extractlog\US_EN_2.2.log
`
The Post-Processor jar can be run using the command:
`java -ms32m -mx4g -Dfile.encoding=UTF-8 -jar <PROJECT XTRCT DIR PATH>\lib\incentives-extract-post-processor.jar -extractPath <PROJECT XTRCT DIR PATH>\Data\Data\ConsumerExtract_3.1_en_US.zip -outPath <PROJECT XTRCT DIR PATH>\Data\ -versions 2.2,2.1,2.0
`
## Performance Tuning
Because of continues increasing record in SQL database, it may be possible (in future) xtrct takes too much long time to finish. 
In this case above configuration can be changed to tweak the performance of XTRCT.

##### For Example
1 - If `records.per.read` is increased by 1.5x times, it is necessary to also increase the runtime memory i.e. mentioned under VM args as `Xmx`. Only increasing the `Xmx` may not improve the performance until application is throwing the OutOfMemory Exception.

2 - Also please make sure that `thread.pool.size` should not be less than 1.5 * (`records.per.read` **/** `records.per.writer`), so the records ready to write may not filling up the memory  and being extracted as soon as they are processed.