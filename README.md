# sentinel
A simplified version of alibaba sentinel. 

## goals
### alibaba sentinel
Alibaba sentinel serve very high qps on cluster level, which may not support some features that consume more physical resource.
### windery sentinel
Windery sentinel serve high qps on node level, which supports some useful features which may consume more physical resource.

## sims & diffs
| Feature               | sentinel(alibaba)                                                                                    | sentinel(windery)                                                                                   |
|-----------------------|------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| cluster level control | supported                                                                                            | not supported                                                                                       |
| flow control          | supported                                                                                            | supported                                                                                           |
| degrade               | supported                                                                                            | supported                                                                                           |
| degrade auto recover  | only check one request                                                                               | support multiple requests                                                                           |
