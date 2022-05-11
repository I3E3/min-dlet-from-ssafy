import React, { useState, Suspense, useEffect } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls, Html } from '@react-three/drei';
import DandelionFlower from '../DandelionFlower';
import DandelionPetalDown from '../DandelionPetalDown';
import Dandelion from '../../Landing/Dandelion';

const dandles = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15];

function FallbackTitle() {
  return (
    <Html center>
      <h1 style={{ color: 'white' }}>
        <br />
        Loading...
        <br />
      </h1>
    </Html>
  );
}

export default function NewPlantAnimation({ endstate }) {
  const [nextstate, SetNext] = useState(0);

  const handleNext = (next) => {
    SetNext(next);
  };

  useEffect(() => {
    console.log(nextstate);
    if (nextstate > 0) endstate(true);
  }, [nextstate]);

  return (
    <Canvas
      frameloop="demand"
      camera={{
        position: [60, -50, 110],
        // <Canvas frameloop="demand" style={{pointerEvents: 'auto', cursor:'pointer'}} pixelRatio={[1, 1]} camera={{ position: [-15, 27, 150],
        fov: 90,
        far: 500,
        near: 10,
      }}
    >
      <ambientLight intensity={0.3} />
      <Suspense fallback={<FallbackTitle />}>
        <DandelionFlower />
        <DandelionPetalDown flag={handleNext} />
        <directionalLight
          position={[0.5, 1, 0.866]}
          intensity={1.7}
          // castShadow
        />

        <directionalLight
          // color="#b6fccd"
          position={[0.5, 10, 0.866]}
          intensity={1.7}
          // castShadow
        />
        {dandles.map((dandle) => {
          return <Dandelion key={dandle} seed={dandle} />;
        })}
        <directionalLight
          position={[-1, -0.3, -0.866]}
          intensity={1}
          // castShadow
        />
      </Suspense>
      <OrbitControls
        enableRotate={true}
        enablePan={false}
        autoRotate={false}
        minDistance={120}
        maxDistance={150}
      />
    </Canvas>
  );
}
