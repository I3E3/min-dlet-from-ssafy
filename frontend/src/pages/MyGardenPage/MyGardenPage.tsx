import MyGardenBody from "containers/MyGarden/MyGardenBody";
import MyGardenFooter from "containers/MyGarden/MyGardenFooter";
import MyGardenHeader from "containers/MyGarden/MyGardenHeader";
import React from "react";

function MyGardenPage() {
  return (
    <div>
      <MyGardenHeader />
      <MyGardenBody />
      <MyGardenFooter />
    </div>
  );
}

export default MyGardenPage;
