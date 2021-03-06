package org.realityforge.webtack;

final class ExitCodes
{
  static final int SUCCESS_EXIT_CODE = 0;
  static final int ERROR_EXIT_CODE = 1;
  static final int ERROR_PARSING_ARGS_EXIT_CODE = 2;
  static final int ERROR_LOADING_CONFIG_CODE = 3;
  static final int ERROR_SAVING_CONFIG_CODE = 4;
  static final int ERROR_SOURCE_EXISTS_CODE = 5;
  static final int ERROR_BAD_SOURCE_NAME_DERIVED_CODE = 6;
  static final int ERROR_SOURCE_DOES_NOT_EXIST_CODE = 7;
  static final int ERROR_SOURCE_FETCH_FAILED_CODE = 8;
  static final int ERROR_EXTRACT_IDL_FAILED_CODE = 9;
  static final int ERROR_SOURCE_NOT_FETCHED_CODE = 10;
  static final int ERROR_IDL_NOT_VALID_CODE = 11;
  static final int ERROR_SAVING_IDL_CODE = 12;
  static final int ERROR_REMOVING_EXISTING_IDL_CODE = 13;
  static final int ERROR_REMOVING_SOURCE_CODE = 14;
  static final int ERROR_SCHEMA_INVALID_CODE = 15;
  static final int ERROR_UNKNOWN_STAGE_CODE = 16;
  static final int ERROR_FAILED_STAGE_PROCESS_CODE = 17;
  static final int ERROR_BAD_PIPELINE_CODE = 18;
  static final int ERROR_DOC_SOURCE_FETCH_FAILED_CODE = 20;
  static final int ERROR_DOC_SOURCE_IO_ERROR_CODE = 21;
  static final int ERROR_DOC_SOURCE_UNEXPECTED_ERROR_CODE = 22;
  static final int ERROR_BAD_TIMESTAMP_READ_ERROR_CODE = 23;
  static final int ERROR_BAD_TIMESTAMP_WRITE_ERROR_CODE = 24;
  static final int ERROR_DOC_SOURCE_TYPE_NOT_PRESENT_CODE = 25;

  private ExitCodes()
  {
  }
}
