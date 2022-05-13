import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function DandelionUp({ dandelUp, petal }) {
  const { scene } = useGLTF(require('assets/Models/two_dandel.glb'));
  const dandle = useRef();
  useEffect(() => {
    console.log(dandelUp);
  }, [dandelUp]);

  useFrame(() => {
    // dandle.current.rotation.y += 0.005;
    if (dandelUp && dandle.current.position.y < 170)
      dandle.current.position.y += 1;
    if (dandle.current.position.y >= 170) {
      setTimeout(() => {
        petal(true);
      }, 1000);
    }
  });
  return (
    <instancedMesh ref={dandle}>
      <primitive position={[-10, -300, 0]} object={scene} scale={8} />
    </instancedMesh>
  );
}
