import instance from "services/axios";

const COMMON = '/dandelions';
export const leftSeedCount = async () => {
  const response = await instance.get(COMMON + '/leftover-seed-count');
  return response.data;
};

export const postContents = async (formData: any) => {
  const result = await instance.post('/dandelions', formData);
  return result;
};
