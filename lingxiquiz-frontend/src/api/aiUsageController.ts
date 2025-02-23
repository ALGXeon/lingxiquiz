// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** getAvailableUses GET /api/aiUsage/get */
export async function getAvailableUsesUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAvailableUsesUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>("/api/aiUsage/get", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
