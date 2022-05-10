import React from 'react';

const Plant = ({ onClick }: any) => {
  const moveScene = () => {
    onClick(1);
  };
  return (
    <div>
      <button onClick={moveScene}>plant Animation</button>
    </div>
  );
};

export default Plant;
