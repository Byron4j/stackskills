package org.byron4j;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

@SpringBootApplication
public class Chapter20Application {

	/**
	 * 限流器注册机
	 */
	@Autowired
	RateLimiterRegistry userRateLimiterRegistry;

	public static void main(String[] args) {
		SpringApplication.run(Chapter20Application.class, args);
	}

	// 全局过滤器
	@Bean("limitGlobalFilter")
	public GlobalFilter limitGlobalFilter(){
		return ((exchange, chain) -> {
			// 获取配置好的限速器 （application.yml 配置好的名字 commonLimiter）
			RateLimiter userRateLimiter = userRateLimiterRegistry.rateLimiter("commonLimiter");

			// 绑定限速器
			Callable<ResultMessage> call = RateLimiter.decorateCallable(userRateLimiter,
					() -> new ResultMessage(true, "PASS"));

			// 尝试获取结果
			Try<ResultMessage> tryResult = Try.of(() -> call.call())
					.recover(ex->new ResultMessage(false, "TOO MANY REQUESTS"));

			// 获取请求结果
			ResultMessage resultMessage = tryResult.get();

			if (resultMessage.isSuccess()) {
				// 没有超流量
				return chain.filter(exchange);
			}else {
				// 限流
				ServerHttpResponse response = exchange.getResponse();
				// 设置响应码
				response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

				// 转换为 JSON
				byte[] bytes = JSONObject.toJSONString(resultMessage).getBytes();
				DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(bytes);
				Mono<Void> voidMono = response.writeWith(Flux.just(dataBuffer));
				return voidMono;
			}
		});
	}

	class ResultMessage{
		private boolean success;
		private String note;

		public ResultMessage() {
		}

		public ResultMessage(boolean success, String note) {
			this.success = success;
			this.note = note;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}
	}



}
