import React, { useEffect, useRef } from 'react';
import { useGLTF } from '@react-three/drei';

export default function Earth() {
  const { scene } = useGLTF(require('assets/Models/earth_with_tree.glb'));
  const earth = useRef();
  useEffect(() => {
    earth.current.rotation.y += 300;
  }, []);
  return (
    <instancedMesh ref={earth}>
      <primitive position={[7, -180, 0]} object={scene} scale={10} />{' '}
    </instancedMesh>
  );
}
