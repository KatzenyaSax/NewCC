# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0.0] - 2026-03-31

### Added
- Initial project structure with 6 microservices + common module
- RESTful API specification based on Alibaba Java Development Handbook
- dafuweng-common module with core infrastructure:
  - R<T> unified response wrapper with Builder pattern
  - PageResult<T> pagination response
  - ErrorCode enum with 60+ business error codes
  - BusinessException and GlobalExceptionHandler
  - EntityConverter base interface for MapStruct
  - MybatisPlusConfig with pagination and auto-fill
  - RedisConfig with Redisson
  - FeignConfig with auth token interceptor
- dafuweng-gateway with JWT authentication filter
- dafuweng-notify with RabbitMQ configuration
