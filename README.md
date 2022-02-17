# amazon-review-filter-api-tests

Amazon review language filter option is chosen for this project. For more information, please check the 'TestPlanDocument.pdf' document.

# Running Tests

## Running Tests Locally
Test runner classes are located in `testRunners` package. In this package `amazonReviewAPITests` xml file is located. For local run, select xml file and run it in the code editor.

# Changing Retry Count
In `RetryAnalyzer` class, change `maxRetryCount` value. Default value is 3 right now.

# Changing Environment Values
In `BaseSettings` class, change desired parameter in `setTestProperties` method