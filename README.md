# Domain Driven Infrastructure PoC

This is a proof of concept for domain driven infrastructure.

Whilst Infrastructure as Code (IaC) defines the exact infrastructure,
there's often rules at a higher level of abstraction behind that
code which is are not defined.

This is a Proof of Concept (PoC) to drive the generation of infrastructure
based on those higher level rules.

## Why Domain Driven?

Domain Driven Design (DDD) is an approach which puts the business domain at the 
heart of Software Development.  Tactical DDD uses a Rich Domain Model to 
model the business domain.  The classes within that domain model collaborate 
to implement the business behaviour.

The parallels between the actual business domain and the model, lead to 
naturally extensible code which is better at facilitating change.

It is our hypothesis that it is possible to bring similar maintainability
and extensibility benefits to infrastructure.

Although infrastructure doesn't necessarily live in the business domain, there
are often business rules which drive which infrastructure is deployed.  This
is the domain being modeled.

Your business domain will determine how you structure your domain.
In the example given below, the aggregate root is a Tier.  Whilst appropriate for some, others may prefer to have a root which relates to a microservice, or related microservices which provide some user facing functionality.
The choice may be based on the way in which your domain is deployed and how it changes over time.

## Our example infrastructure domain

(Domain language is `highlighted`)

In this domain our infrastructure consists of multiple `Tier`s, each of which
consists of multiple `Components`.  Each `Component` `exposes` a number of `Endpoint`s
which may be `exposed` by a load balancer, of which there are as few as possible per `Tier`.

At present the `Components` in the domain are all `Instance Based`, i.e. they are
realised by a scaling group of virtual machines.

`Components` may `depend` on their own `Endpoints`, the `Endpoints` of other `Components`,
or `Endpoints` `exposed` by the Load Balancer associated with their `Tier` or in another
`Tier`.

Network rules should be in place to ensure `Endpoints` can only be accessed by
dependent `Components`.

### AWS specifics

At present this PoC is AWS only - although there is some abstraction which would allow
later extension.

Our domain has some AWS specific rules about the infrastructure:

* A security group should be created for each `Component`
    * allowing egress to the `Endpoints` the `Components` is `dependent` on
      to the security groups of the relevant `Component` or load balancer
    * allowing ingress for the `Endpoints` of the `Component` from the security groups
      of Components which are directly dependent
    * allowing ingress from the load balancer where an `Endpoint` is exposed from the `Tier`

* An ALB should be created if any of the `Components` within the `Tier` `expose` an HTTP `Endpoint` 
* An NLB should be created if any of the `Components` within the `Tier` `expose` a TCP (non-HTTP) `Endpoint`

#### VPC and Subnets

The VPC and subnets are created by the Networking module.
A CIDR range is provided for the stack (dev in this example).  The networking module then divides this into the number of subnets needed based on the number of tiers.

## Running the code

1. First login to a local backend

`pulumi login --local`

2. Build the code and install to local maven repo

`cd [REPO_ROOT]`
`mvn clean install`

3. Build the tiers in order

Execute `pulumi up` on:

* networking
* data-tier
* compute-tier
