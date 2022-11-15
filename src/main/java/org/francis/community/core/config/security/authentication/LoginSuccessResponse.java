package org.francis.community.core.config.security.authentication;

import lombok.Data;

@Data
public class LoginSuccessResponse {

  private String token;
  private String username;
}
