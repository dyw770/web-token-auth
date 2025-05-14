export enum ApiMethod {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  DELETE = 'DELETE',
  PATCH = 'PATCH',
  OPTIONS = 'OPTIONS',
  HEAD = 'HEAD',
  TRACE = 'TRACE',
  ALL  = 'ALL'
}

export enum MatchType {
  /**
   * 正则
   */
  REGEX = 'REGEX',

  /**
   * ant
   */
  ANT = 'ANT'
}
