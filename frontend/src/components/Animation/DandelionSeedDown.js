import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';
export default function DandelionSeedDown({ flag }) {
  const { scene } = useGLTF(require('assets/Models/seed_color.glb'));
  const [down, SetDown] = useState(1);
  const [size, SetSize] = useState(50);
  const [clicked, click] = useState(true);

  const petal = useRef();

  useFrame(() => {
    if (petal.current.position.y > -200) {
      petal.current.position.y -= down;
      SetSize(size + 0.01);
    } else {
      flag(true);
      SetSize(size + 0.01);
      petal.current.position.y -= down;
    }
  });

  useEffect(() => {}, [size]);

  return (
    <instancedMesh ref={petal}>
      <primitive
        position={[-20, 100, -30]}
        object={scene}
        scale={6}
        onClick={(event) => {
          click(!clicked);
        }}
      />
    </instancedMesh>
  );
}
