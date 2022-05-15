import instance from "services/axios";

const COMMON = "/user/admin";
export const getPendingApplicationList = async () => {
  const response = await instance.get(COMMON + "/mypage/donation");
  // console.log(response);
  return response.data;
};

export const postContents = async (formData: any) => {
  const result = await instance.post(`/donation`, formData);
  return result;
};
