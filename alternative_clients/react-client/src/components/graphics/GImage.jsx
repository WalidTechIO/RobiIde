import { useState, useEffect } from 'react';

export default function GImage({image}) {

  const [img, setImg] = useState(<img alt="Loading..." />)

  File.prototype.convertToBase64 = function (callback) {
    var reader = new FileReader();
    reader.onloadend = function (e) {
      callback(e.target.result, e.target.error);
    };
    reader.readAsDataURL(this);
  }

  useEffect(() => {
    let imgindex = -1
    const images = document.getElementById('images_selector')?.files
    if (images) {
      for (let i = 0; i < images.length; i++) {
        if (images[i].name === image.path) {
          imgindex = i
        }
      }
    }

    if (imgindex != -1) {
      images[imgindex].convertToBase64((base64) => {
        setImg(<img src={base64} width={image.dimension.width} height={image.dimension.height}/>)
      })
    } else {
      setImg(<img alt="Image not found in images list" />)
    }
  }, [image.dimension])

  const style = {
    position: "absolute",
    top: image.position.height,
    left: image.position.width,
    backgroundColor: image.color,
    maxWidth: 'calc(100% - ' + image.position.width + 'px)',
    maxHeight: 'calc(100% - ' + image.position.height + 'px)',
    overflow: 'hidden'
  }

  return <div style={style}>
    {img}
  </div>
}
