import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function Earth() {
  const { scene } = useGLTF(require('assets/Models/two_dandel.glb'));
  const dandle = useRef();
  useFrame(() => (dandle.current.rotation.y += 0.005));
  return (
    <instancedMesh ref={dandle}>
      <primitive position={[-10, -150, 0]} object={scene} scale={8} />
    </instancedMesh>
  );
}
