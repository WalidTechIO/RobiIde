import Rect from "./Rect.jsx";
import { GImage } from "./GImage.jsx";

export default function ObjectRenderer({object, images}) {

    //World rendering
    if(object.type === "WORLD") {

        if (object.dimension.width < 0) {
            object.dimension.width = 200
        }
        if (object.dimension.height < 0) {
            object.dimension.height = 200
        }

        const style = {
            position: "relative",
            backgroundColor: object.color,
            width: `${object.dimension.width}px`,
            height: `${object.dimension.height}px`,
        }

        return <div style={style}>
            {object.childrens.map(children => {
                return (<ObjectRenderer key={children} object={children} images={images} />)
            })}
        </div>
    }

    //Common objects
    return <>
        {object.type === "RECT" && <Rect rectangle={object} images={images}/>}
        {object.type === "IMAGE" && <GImage image={object} images={images} />}
        {/*TODO*/}
    </>

}