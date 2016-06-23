# appdirect_test

This sample webapp was written in order to answer the AppDirect Backend Coding Challenge. The idea was to integrate a sample web application to the AppDirect marketplace by implementing a few API calls (at least the Subscription.Create and Subscription.Cancel events) and single sign-on through OpenId 2.0.

## Technologies

I chose the following modules as my technology stack:

- Wildfly 10 + JSF 2.1 + CDI
- JBoss Picketlink (Security and identity management)
- JAX-RS for the restful services
- 3pillarlabs's SocialAuth for OpenId authentication (based on openid4java)
- Signpost for signing messages with oauth 1.0a
- Bootstrap + JQuery for the layout

## Sample application

Basing myself on the wildfly kitchensink application and the picketlink quickstart, i quickly developed a dummy web application that supports user login with username/password credentials. This application was then integrated as specified.

## How to run

The application is defined by a pom and can be built and deployed using Apache Maven. 

### Prerequisites

- Maven 3.3.x
- Java 8 JDK
- A running wildfly 10 server 

### Starting the server

_See the Wildfly kitchensink quickstart guide [README.md](https://github.com/wildfly/quickstart/blob/10.x/kitchensink/README.md) for details on how to setup wildfly and deploy applications to it using maven_

	WILDFLY_HOME\bin\standalone.bat

### Deploy the application

	mvn clean install -Pwildfly wildfly:deploy

### Undeploy the application

	mvn -Pwildfly wildfly:undeploy

## Work done

While I didn't find the challenge to be very hard, it did take me more than the 8 to 10 hours I was told were expected of me. There is a considerable amount of details involved in selecting the technologies, implementing/adapting a sample application, integrating aging standards (oauth 1.0, openid 2.0) with hackish libraries, getting everything online and successfully test everything. Given that I work full time, I had to limit myself to the minimum requirements:

- OpenId single sign-on
- Event retrieval OAuth signature using the secret key
- Subscriptions API: Create and Cancel 

## Caveats

- StackExchange OpenId does not work. It refuses the response during the 2/4 step of the validation (ClaimeId validation). Other providers like AppDirect or Yahoo work perfectly. Here is the error for StackExchange:

	No service element found to match the ClaimedID / OP-endpoint in the assertion.

- When the application runs on OpenShift, the openid4java library fails to connect to the identity provider. It might be because it selects the wrong local interface for its socket binding. Other outgoing http requests - such as the event retrieval from AppDirect - seem to work. I did not have time to find a workaround to this issue.
