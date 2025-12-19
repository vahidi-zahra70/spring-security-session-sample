Spring Security Session Examples (Redis, Hazelcast, Default)

This project is a learning-oriented Spring Boot application that demonstrates:

How Spring Security authentication works internally

How HTTP sessions are created and managed

How to store sessions using Spring Session

How to implement a custom 2-step (OTP-based) login flow

How to switch session storage between Redis, Hazelcast, and default in-memory sessions

The same authentication logic is reused across different session backends to highlight the differences and similarities.

Branches Overview

This repository contains three independent branches, each focusing on a different setup:

1️⃣ session-form-login

Goal:
Understand Spring Security’s default behavior.

Features:

Default formLogin()

No custom filters

No Spring Session

Sessions stored in the servlet container (Tomcat)

Use this branch if you want to:

See how Spring Security works out of the box

Compare custom authentication vs default login

2️⃣ session-redis-repository

Goal:
Learn how to use Spring Session with Redis and implement a 2-step OTP login.

Features:

Custom UsernamePasswordAuthenticationFilter

Two-step authentication:

Step 1: Username/password → OTP sent

Step 2: OTP verification → session created

Two AuthenticationProviders:

UsernamePasswordAuthenticationProvider

OtpAuthenticationProvider

Sessions stored in Redis

Sliding session timeout (10 minutes of inactivity)

Session repository:

RedisSessionRepository

3️⃣ session-hazelcast-repository

Goal:
Use the same authentication logic but store sessions in Hazelcast instead of Redis.

Features:

Same custom filter and providers as redis-session

Uses Spring Session Hazelcast

Sessions stored in a Hazelcast distributed map

Can be inspected via Hazelcast Management Center
