package endolabs.salesforce.bulkv2;

import java.util.concurrent.TimeUnit;

import endolabs.salesforce.bulkv2.response.CreateJobResponse;
import endolabs.salesforce.bulkv2.response.GetAllJobsResponse;
import endolabs.salesforce.bulkv2.response.GetJobInfoResponse;
import endolabs.salesforce.bulkv2.response.JobInfo;
import endolabs.salesforce.bulkv2.type.JobTypeEnum;
import endolabs.salesforce.bulkv2.type.OperationEnum;

public class bulk_api_v2 {

      public static void main(String[] args) {
            try{
            Bulk2Client client = new Bulk2ClientBuilder()
                    .withPassword("3MVG9e2mBbZnmM6n0NnLO45qYlilEgx.FrqcepXHLJFes6hCNtq14eIKq7bNBfnkjWETHwHv1vT4U5.8oTlWu", "", "khoa.h@vinpearl.com.mulesoft", "")
                    .useSandbox()
                    .build();
            //create JOB
            CreateJobResponse createJobResponse = client.createJob("Account", OperationEnum.INSERT);
            String jobId = createJobResponse.getId();

            String csv = "Name,Description,NumberOfEmployees\n" +
                    "TestAccount1,Description of TestAccount1,30\n" +
                    "TestAccount2,Another description,40\n" +
                    "TestAccount3,Yet another description,50";
            client.uploadJobData(jobId, csv);

            // When using a separate request to upload data, make sure to close the job
            JobInfo closeJobResponse = client.closeJob(jobId);

            while (true) {
                TimeUnit.SECONDS.sleep(1);

                GetJobInfoResponse jobInfo = client.getJobInfo(jobId);
                if (jobInfo.isFinished()) {
                    break;
                }
            }
            
            GetAllJobsResponse jobs = client.getAllJobs(request -> request.withJobType(JobTypeEnum.BULK_API_2_0));
            for (JobInfo jobInfo : jobs.getRecords()) {
                System.out.println(jobInfo);
            }
            
            }catch(Exception e){
                  System.out.println("EXCEPTION: " + e.getMessage() + " = "
                                                + e.getLocalizedMessage() + " = " + e.getClass() ) ;
                  StackTraceElement[] traces = e.getStackTrace();
                  for(StackTraceElement trace:traces){
                        System.out.println(trace);
                  }
            }

      }

}
