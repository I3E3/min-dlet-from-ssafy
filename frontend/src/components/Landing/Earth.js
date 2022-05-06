import React from 'react';
import { useGLTF } from "@react-three/drei";


export default function Earth () {
  const { scene } = useGLTF(require('assets/Models/earth_modi1_compressed.glb'))


  return (
        <primitive position={[0, -110, 0]} object={scene} scale={0.1} />
  );
}