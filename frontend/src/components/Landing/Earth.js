import React from 'react';
import { useGLTF } from '@react-three/drei';

export default function Earth() {
  const { scene } = useGLTF(require('assets/Models/earth_with_cloud_tree.glb'));

  return <primitive position={[1, -180, 0]} object={scene} scale={10} />;
}
